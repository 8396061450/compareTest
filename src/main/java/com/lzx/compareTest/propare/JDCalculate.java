package com.lzx.compareTest.propare;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lzx.compareTest.common.Task;
import com.lzx.compareTest.pojo.Detail;
import com.lzx.compareTest.pojo.ZQDetail;
import com.lzx.compareTest.utils.RegexUtils;
import com.lzx.compareTest.utils.StringUtils;

public class JDCalculate implements Runnable,Task{
	public static Logger log = LogManager.getLogger("infoFile");
	/**
	 * 京东价格计算： 总价--经过折扣---使用优惠券[满减券，满折券]---自己的券
	 *  同一折扣下，同一 减的钱数   num越小 越好
	 *  同一个物品  计算 p/num 越小越好
	 *  不同物品 主要计算 (K+Plus)/个数 【重要的选择是  最后的个数 个数越多也好】
	 *  主要计算 (单价*总价-券)/总价 最小
	 *  
	 * 
	 * @param 
	 * @param price
	 * @param num
	 * @return
	 */
	private static String zkregex="[\\s\\S].*?([\\d]+)[\\s\\S].*?([\\d\\.]+)[\\s\\S]*";
	private static String quan1regex="[\\s\\S].*?([\\d\\.]+)[\\s\\S].*?([\\d\\.]+)[\\s\\S]*";
	private static String quan2regex="[\\s\\S].*?([\\d\\.]+)[\\s\\S].*?([\\d\\.]+)[\\s\\S]*";
	
	private Detail detail;
	private List<ZQDetail> zd;
	public JDCalculate(Detail detail) {
		this.detail=detail;
	}
	
	public void run(){
		zd=new ArrayList<ZQDetail>();
		List<String> zhekou=detail.getZhekou();
		List<String> quan=detail.getQuan();
		float price=detail.get_price();
		int num=detail.get_num();
		String url=detail.get_url();
		float uniprice=detail.get_unitprice();
		List<Float[]> zk=handleZK(zhekou);
		List<Float[]> q=handleQuan(quan);
		//保存没有折扣的
		ZQDetail temp=new ZQDetail();
		temp.setDetail(detail);
		zd.add(temp);
		log.info(url+":"+detail.getName()+":"+price+":"+num+":"+detail.get_unitprice());
		if(zk!=null&&zk.size()>0){
			for(int i=0;i<zk.size();i++){
				Float neednum=zk.get(i)[0];//使用打折的个数
				uniprice=uniprice*zk.get(i)[1]/10;
				log.info("使用折扣："+zk.get(i)[0]+"享"+zk.get(i)[1]);
				handleByQuan(price, uniprice, q,num,neednum,zk.get(i)[1]/10);
			}
		}
		if(zk==null||zk.size()==0){
			handleByQuan(price, uniprice, q,num,1,1);
		}
	}
	/**
	 * 
	 * @param price 原价
	 * @param uniprice 打折单价
	 * @param q
	 * @param num  需要个数
	 */
	private void handleByQuan(float price, float uniprice, List<Float[]> q,int num,float neednum,float zk) {
		log.info("   (单独使用折扣)   minUniPirce="+(uniprice));
		//只享受折扣的价格
		ZQDetail t=new ZQDetail();
		t.setDetail(detail);
		t.setZk(zk+"");
		t.setNeedNum(neednum+"");
		t.setUniPrice(uniprice+"");
		zd.add(t);
		if(q!=null){
			for(int i=0;i<q.size();i++) {
				Float[] qi=q.get(i);
				if(qi[1]<10) {//满多少打折券
					if(price*neednum*zk>qi[0]){//直接满足
						ZQDetail temp=new ZQDetail();
						temp.setDetail(detail);
						temp.setNeedNum(neednum+"");
						temp.setUniPrice(uniprice+"");
						temp.setQuan(qi[0]+"-"+qi[1]);
						temp.setAllPrice(price*neednum*zk*qi[1]/10+"");
						temp.setAlNum(num*neednum+"");
						temp.setUniPrice((uniprice*qi[1]/10)+"");
						zd.add(temp);
						log.info("   ("+qi[0]+"享"+qi[1]+"折)  购买个数 "+ neednum +"  总价："+price*neednum*zk*qi[1]/10+" 数量:"+num*neednum+" 单价最小：minUniPirce="+(uniprice*qi[1]/10));
					}else{//需要添加购买个数  得重新计算满足当前折扣的单价了
						int minMax=minQuan1Num(price,qi[0],zk);//需要的个数
						ZQDetail temp=new ZQDetail();
						temp.setDetail(detail);
						temp.setNeedNum(minMax+"");
						temp.setUniPrice(uniprice+"");
						temp.setQuan(qi[0]+"-"+qi[1]);
						temp.setAllPrice(price*minMax*zk*qi[1]/10+"");
						temp.setAlNum(num*minMax+"");
						temp.setUniPrice((uniprice*qi[1]/10)+"");
						zd.add(temp);
						log.info("   ("+qi[0]+"享"+qi[1]+"折) 购买个数 "+ minMax +"  总价："+price*minMax*zk*qi[1]/10+ " 数量:"+num*minMax+"  单价最小：minUniPirce="+(uniprice*qi[1]/10));
					}
				}else{//满多少-多少
					if(price*neednum*zk>qi[0]){ 
						ZQDetail temp=new ZQDetail();
						temp.setDetail(detail);
						temp.setNeedNum(neednum+"");
						temp.setQuan(qi[0]+"-"+qi[1]);
						temp.setAllPrice((price*neednum*zk-qi[1])+"");
						temp.setAlNum(num*neednum+"");
						temp.setUniPrice((uniprice-qi[1]/(neednum*num))+"");
						temp.setPn(qi[1]/(neednum*num)+"");
						zd.add(temp);
						log.info("   ("+qi[0]+"-"+qi[1]+") 购买个数 "+ neednum +"  总价："+(price*neednum*zk-qi[1])+" 数量:"+num*neednum+"   单价最小：  minUniPirce="+(uniprice-qi[1]/(neednum*num))+"   p/n="+qi[1]/(neednum*num));
					}else {
						int minMax=minQuan1Num(price,qi[0],zk);
						ZQDetail temp=new ZQDetail();
						temp.setDetail(detail);
						temp.setNeedNum(minMax+"");
						temp.setQuan(qi[0]+"-"+qi[1]);
						temp.setAllPrice((price*minMax*zk-qi[1])+"");
						temp.setAlNum(num*minMax+"");
						temp.setUniPrice((uniprice-qi[1]/(minMax*num))+"");
						temp.setPn(qi[1]/(minMax*num)+"");
						zd.add(temp);
						log.info("   ("+qi[0]+"-"+qi[1]+") 购买个数 "+ minMax +"  总价："+(price*minMax*zk-qi[1])+" 数量:"+num*minMax+"   单价最小：   minUniPirce="+(uniprice-qi[1]/(minMax*num))+"   p/n="+qi[1]/(minMax*num));
					}
				}
			}
		}
	}
	private static List<Float[]> handleZK(List<String> zhek){
		if(zhek==null||zhek.size()==0) {
			return null;
		}
		List<Float[]> result=new ArrayList<Float[]>();
		for(int i=0;i<zhek.size()&&zhek.get(i)!=null;i++){
			if(zhek.get(i).contains("折")||zhek.get(i).contains("享")) {
				String[] zheks=zhek.get(i).split("[;；]");
				for(int j=0;j<zheks.length;j++) {
					Float[] zk=RegexUtils.regexFloatMathgroup(zheks[j],zkregex, 1, 2);
					if(zk!=null) {
						result.add(zk);
					}else {
						String[] zhekis=zheks[j].split("[,，]");
						for(int m=0;i<zhekis.length;m++) {
							zk=RegexUtils.regexFloatMathgroup(zhekis[m],zkregex, 1, 2);
							if(zk!=null) {
								result.add(zk);
							}
						}
						
					}
				}
			
			}
		}
		return result;
	}
	
	private static List<Float[]> handleQuan(List<String> quan){
		if(quan==null||quan.size()==0) {
			return null;
		}
		List<Float[]> result=new ArrayList<Float[]>();
		for(int i=0;i<quan.size()&&quan.get(i)!=null;i++){
			
			Float[] zk=RegexUtils.regexFloatMathgroup(quan.get(i),quan1regex, 1, 2);
			if(zk==null){
				zk=RegexUtils.regexFloatMathgroup(quan.get(i),quan2regex, 1, 2);
			}
			if(zk!=null) {
				result.add(zk);
			}
		}
		return result;
	}

	private static boolean check(String[] s) {
		if(s==null||s.length!=2||!StringUtils.notEmpty(s[0])||StringUtils.notEmpty(s[1])) {
			return false;
		}
		return true;
	}
	/**
	 * 计算最小的个数
	 * @param price
	 * @param all
	 * @param plus
	 * @return
	 */
	private static int minQuan1Num(float price,float all,float zk){
		return (int)((all/(price*zk))+1);
	}
}
