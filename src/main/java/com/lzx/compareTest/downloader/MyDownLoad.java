package com.lzx.compareTest.downloader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.UrlUtils;

public class MyDownLoad {
	public static Page downloadPage(Site site,String url) {
		Request request=new Request(url);
		CloseableHttpResponse httpResponse;
		try {
			httpResponse = (CloseableHttpResponse) getHttpClient(site).execute(getHttpUriRequest(request,site,site.getHeaders(),null));
			return handleResponse(request,httpResponse);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	 private static HttpClient getHttpClient(Site site) {
		 HttpClientGenerator httpClientGenerator = new HttpClientGenerator();
		 return httpClientGenerator.getClient(site, null);
	}

	protected static HttpUriRequest getHttpUriRequest(Request request, Site site, Map<String, String> headers,HttpHost proxy) {
	        RequestBuilder requestBuilder = RequestBuilder.get().setUri(request.getUrl());
	        if (headers != null) {
	            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
	                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
	            }
	        }
	        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
	                .setConnectionRequestTimeout(site.getTimeOut())
	                .setSocketTimeout(site.getTimeOut())
	                .setConnectTimeout(site.getTimeOut())
	                .setCookieSpec(CookieSpecs.BEST_MATCH);
	        if (proxy !=null) {
				requestConfigBuilder.setProxy(proxy);
				request.putExtra(Request.PROXY, proxy);
			}
	        requestBuilder.setConfig(requestConfigBuilder.build());
	        return requestBuilder.build();
	 }
	
	protected static Page handleResponse(Request request, HttpResponse httpResponse) throws IOException {
        String content = getContent(null, httpResponse);
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        return page;
    }
	
	 protected static String getContent(String charset, HttpResponse httpResponse) throws IOException {
	        if (charset == null) {
	            byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
	            String htmlCharset = getHtmlCharset(httpResponse, contentBytes);
	            if (htmlCharset != null) {
	                return new String(contentBytes, htmlCharset);
	            } else {
	                return new String(contentBytes);
	            }
	        } else {
	            return IOUtils.toString(httpResponse.getEntity().getContent(), charset);
	        }
	    }
	 protected static String getHtmlCharset(HttpResponse httpResponse, byte[] contentBytes) throws IOException {
	        String charset;
	        String value = httpResponse.getEntity().getContentType().getValue();
	        charset = UrlUtils.getCharset(value);
	        if (StringUtils.isNotBlank(charset)) {
	            return charset;
	        }
	        Charset defaultCharset = Charset.defaultCharset();
	        String content = new String(contentBytes, defaultCharset.name());
	        if (StringUtils.isNotEmpty(content)) {
	            Document document = Jsoup.parse(content);
	            Elements links = document.select("meta");
	            for (Element link : links) {
	                // 2.1銆乭tml4.01 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	                String metaContent = link.attr("content");
	                String metaCharset = link.attr("charset");
	                if (metaContent.indexOf("charset") != -1) {
	                    metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
	                    charset = metaContent.split("=")[1];
	                    break;
	                }
	                else if (StringUtils.isNotEmpty(metaCharset)) {
	                    charset = metaCharset;
	                    break;
	                }
	            }
	        }
	        return charset;
	    }
}
