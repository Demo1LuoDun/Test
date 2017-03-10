package cn.jbit.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReportsUtil {
	/**
	 * 	输入网站地址，获取该网站源码
	 * @param url 网站地址 
	 * @param encoding  网站使用的编码方式
	 * @return 网站源码
	 */
	public static String getResouceByUrl(String url,String encoding){
		StringBuffer buffer=new StringBuffer();
		URL urlobj=null;
		URLConnection uc=null;
		InputStreamReader isr=null;
		BufferedReader reader=null;
		try {
			urlobj=new URL(url);
			uc=urlobj.openConnection();
			isr=new InputStreamReader(uc.getInputStream(),encoding);
			reader=new BufferedReader(isr);
			
			String temp=null;
			while ((temp=reader.readLine())!=null) {
				buffer.append(temp+"\n");
			}
			
			File ff=new File("d:/index.html");
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ff)));
			writer.write(buffer.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
				if(isr!=null){
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}
	/**
	 * 
	 * @param url 网站地址 
	 * @param encoding 网站使用的编码方式
	 * @return 放回封装的数据 [{num=122, tex=教育培训}, {num=7, tex=本地服务}, {num=5, tex=二手物品}, {num=1, tex=兼职招聘}]
	 */
	public static List<HashMap<String,String>> show(String url,String encoding){
		String html=getResouceByUrl(url, encoding);
		List<HashMap<String,String>> lists=new ArrayList<HashMap<String,String>>();
		Document document=Jsoup.parse(html);
		//获取网站上你想获取数据的div，可以根据id,class,name获取（类似于js）
		Element element=document.getElementsByClass("sear-category").get(0);
		Elements elements=element.getElementsByTag("a");
		//循环这个div封装数据
		for (Element ele : elements) {
			HashMap<String,String> map=new HashMap<String,String>();
			String text[] =ele.text().split("\\(");
			String tex=text[0];
			String num=text[1].replaceAll("\\)", "");
			map.put("tex", tex);
			map.put("num",num);
			lists.add(map);
		}
		return lists;
	}
	public static void main(String[] args) {
		String html=getResouceByUrl("http://fashion.qq.com/", "utf-8");
		
	}
}
