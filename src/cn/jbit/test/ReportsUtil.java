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
	 * 	������վ��ַ����ȡ����վԴ��
	 * @param url ��վ��ַ 
	 * @param encoding  ��վʹ�õı��뷽ʽ
	 * @return ��վԴ��
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
	 * @param url ��վ��ַ 
	 * @param encoding ��վʹ�õı��뷽ʽ
	 * @return �Żط�װ������ [{num=122, tex=������ѵ}, {num=7, tex=���ط���}, {num=5, tex=������Ʒ}, {num=1, tex=��ְ��Ƹ}]
	 */
	public static List<HashMap<String,String>> show(String url,String encoding){
		String html=getResouceByUrl(url, encoding);
		List<HashMap<String,String>> lists=new ArrayList<HashMap<String,String>>();
		Document document=Jsoup.parse(html);
		//��ȡ��վ�������ȡ���ݵ�div�����Ը���id,class,name��ȡ��������js��
		Element element=document.getElementsByClass("sear-category").get(0);
		Elements elements=element.getElementsByTag("a");
		//ѭ�����div��װ����
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
