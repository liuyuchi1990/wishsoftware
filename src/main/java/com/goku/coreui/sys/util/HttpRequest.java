/**
 *
 * @project ApiService
 * @filename HttpRequest.java
 * @date 2015年8月16日
 * @author KeShanqiang
 *
 */

package com.goku.coreui.sys.util;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import com.thoughtworks.xstream.XStream;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

public class HttpRequest {
//	/**
//	 * 向指定URL发送GET方法的请求
//	 *
//	 * @param url
//	 *            发送请求的URL
//	 * @param param
//	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
//	 * @return URL 所代表远程资源的响应结果
//	 */
//	public static String sendGet(String url, String param) {
//		String result = "";
//		BufferedReader in = null;
//		try {
//			String urlNameString = url + "?" + param;
//			URL realUrl = new URL(urlNameString);
//			// 打开和URL之间的连接
//			URLConnection connection = realUrl.openConnection();
//
//			// 设置通用的请求属性
//			connection.setRequestProperty("accept", "*/*");
//			connection.setRequestProperty("connection", "Keep-Alive");
//			connection.setRequestProperty("user-agent",
//					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			// 建立实际的连接
//			connection.connect();
//			// 定义 BufferedReader输入流来读取URL的响应
//			in = new BufferedReader(new InputStreamReader(
//					connection.getInputStream()));
//			String line;
//			while ((line = in.readLine()) != null) {
//				result += line;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		// 使用finally块来关闭输入流
//		finally {
//			try {
//				if (in != null) {
//					in.close();
//				}
//
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 向指定 URL 发送POST方法的请求
//	 *
//	 * @param url
//	 *            发送请求的 URL
//	 * @param param
//	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
//	 * @return 所代表远程资源的响应结果
//	 */
//	public static String sendPost(String url, String param) {
//		PrintWriter out = null;
//		BufferedReader in = null;
//		String result = "";
//		try {
//			URL realUrl = new URL(url);
//			// 打开和URL之间的连接
//			URLConnection conn = realUrl.openConnection();
//			// 设置通用的请求属性
//			conn.setRequestProperty("authorization", "Authorization: Basic token=0abf1040cda747f1bd724719fd2c8496");
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent",
//					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			// 发送POST请求必须设置如下两行
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			// 获取URLConnection对象对应的输出流
//			out = new PrintWriter(conn.getOutputStream());
//			out.print(param);
//			// flush输出流的缓冲
//			out.flush();
//			// 定义BufferedReader输入流来读取URL的响应
//			in = new BufferedReader(
//					new InputStreamReader(conn.getInputStream()));
//			String line;
//			while ((line = in.readLine()) != null) {
//				result += line;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		//使用finally块来关闭输出流、输入流
//		finally{
//			try{
//				if(out!=null){
//					out.close();
//				}
//				if(in!=null){
//					in.close();
//				}
//			}
//			catch(IOException ex){
//				ex.printStackTrace();
//			}
//		}
//		return result;
//	}
//	private static final int socketTimeout = 10000;
//
//	//传输超时时间，默认30秒
//	private static final int connectTimeout = 30000;
//	/**
//	 * post请求
//	 * @throws IOException
//	 * @throws ClientProtocolException
//	 * @throws NoSuchAlgorithmException
//	 * @throws KeyStoreException
//	 * @throws KeyManagementException
//	 * @throws UnrecoverableKeyException
//	 */
//	public static String sendPost(String url, Object xmlObj) throws ClientProtocolException, IOException, UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
//
//
//
//		HttpPost httpPost = new HttpPost(url);
//		//解决XStream对出现双下划线的bug
//		XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
//		xStreamForRequestPostData.alias("xml", xmlObj.getClass());
//		//将要提交给API的数据对象转换成XML格式数据Post给API
//		String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
//
//		//得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
//		StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
//		httpPost.addHeader("Content-Type", "text/xml");
//		httpPost.setEntity(postEntity);
//
//		//设置请求器的配置
//		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
//		httpPost.setConfig(requestConfig);
//
//		HttpClient httpClient = HttpClients.createDefault();
//		HttpResponse response = httpClient.execute(httpPost);
//		HttpEntity entity = response.getEntity();
//		String result = EntityUtils.toString(entity, "UTF-8");
//		return result;
//	}
//	/**
//	 * 自定义证书管理器，信任所有证书
//	 * @author pc
//	 *
//	 */
//	public static class MyX509TrustManager implements X509TrustManager {
//		@Override
//		public void checkClientTrusted(
//				java.security.cert.X509Certificate[] arg0, String arg1)
//				throws java.security.cert.CertificateException {
//			// TODO Auto-generated method stub
//
//		}
//		@Override
//		public void checkServerTrusted(
//				java.security.cert.X509Certificate[] arg0, String arg1)
//				throws java.security.cert.CertificateException {
//			// TODO Auto-generated method stub
//
//		}
//		@Override
//		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//	}
}