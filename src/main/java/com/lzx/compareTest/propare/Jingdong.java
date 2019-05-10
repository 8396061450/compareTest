package com.lzx.compareTest.propare;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lzx.compareTest.pojo.Detail;
import com.lzx.compareTest.queue.BlockingQueueHandle;
import com.lzx.compareTest.utils.PojoCreateUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
/**
 * 价格的获取   http://p.3.cn/prices/mgets?skuIds=J_1419126
 * [{"cbf":"0","id":"J_1419126","m":"225.00","op":"129.00","p":"129.00","tpp":"110.00","up":"tpp"}]
 * //club.jd.com/comment/productCommentSummaries.action?referenceIds=
 * http://pm.3.cn/prices/pcpmgets?skuIds=J_526827
 * http://c.3.cn/product/tag?skuIds=J_526827
 * 优惠券：满 200减100 满多少享折
 * 折扣：满几件打几折
 * 
 * @author a8396
 *
 */
public class Jingdong implements PageProcessor{
	public static Logger log = LogManager.getLogger("infoFile");
	public Jingdong() {
		super();
	}

	private final String listRegex="https://search.jd.com/Search?";
	private final String detailRegex="https://item.jd.com/[\\d]+.html";
	private Site site = Site.me().setUserAgent("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)").setRetryTimes(3).setSleepTime(100);
	public void process(Page page) {
		String url=page.getRequest().getUrl();
		if(url.contains(listRegex)) {//List 页面
			log.info("列表页："+url);
			List<String> urls = page.getHtml().links().all();
			for(int i=0;i<urls.size();i++) {
				if(urls.get(i).matches(detailRegex)) {
					Request request=new Request(urls.get(i));
					page.addTargetRequest(request);
				}
			}
		}else if(url.matches(detailRegex)){//详情页面
			Detail detail=new Detail();
			detail.set_pc("京东");
			detail.set_url(url);
			try {
				PojoCreateUtils.createPojo(detail, page);
				detail.calculate();
				//JDCalculate.calculate(detail);
				BlockingQueueHandle.putTask(new JDCalculate(detail));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Site getSite() {
		return site;
	}

}
