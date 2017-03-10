package com.ai.customer.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileUploadController {

	/*
	 * SpringMVC�е��ļ��ϴ�
	 * 
	 * @��һ��������SpringMVCʹ�õ���commons-fileuploadʵ�֣��ʽ������������Ŀ��
	 * 
	 * @�����õ�����commons-fileupload-1.2.1.jar��commons-io-1.3.2.jar
	 * 
	 * @�ڶ�����spring-mvx������MultipartResolver�����������ڴ˼�����ϴ��ļ����������� <bean
	 * id="multipartResolver"
	 * class="org.springframework.web.multipart.commons.CommonsMultipartResolver"
	 * > <!-- �����ϴ��ļ������ߴ�Ϊ10MB --> <property name="maxUploadSize">
	 * <value>10000000</value> </property> </bean>
	 * ����������Controller�ķ��������MultipartFile�������ò������ڽ��ձ���file���������
	 * ���Ĳ�����дǰ̨����ע��enctype="multipart/form-data"�Լ�<input type="file"
	 * name="****"/> ����ǵ����ļ� ֱ��ʹ��MultipartFile ����
	 */

	/********************** �ϴ����� **************************/
	@RequestMapping("/upload.do")
	public ModelAndView upload(String name,@RequestParam("file") MultipartFile[] file,
			HttpServletRequest request) throws IllegalStateException,
			IOException {
		
		String realPath = request.getSession().getServletContext()
				.getRealPath("/uploadFile");

		File pathFile = new File(realPath);

		if (!pathFile.exists()) {
			// �ļ��в��� �����ļ�
			pathFile.mkdirs();
		}
		for (MultipartFile f : file) {
			if(f!=null && f.getSize()>0){
				System.out.println("�ļ����ͣ�" + f.getContentType());
				System.out.println("�ļ����ƣ�" + f.getOriginalFilename());
				System.out.println("�ļ���С:" + f.getSize());
				System.out.println(".................................................");
				// ���ļ�copy�ϴ���������
				f.transferTo(new File(realPath + "/" + f.getOriginalFilename()));
				// FileUtils.copy
			}
		}
		// ��ȡmodelandview����
		ModelAndView view = new ModelAndView();
		view.setViewName("redirect:index.jsp");
		return view;
	}

	/******** ���ش��� *************/
	@RequestMapping(value = "download.do")
	public ModelAndView download(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// String storeName = "Spring3.xAPI_zh.chm";
		String storeName = "705.jpg";
		String contentType = "application/octet-stream";
		FileUploadController
				.download(request, response, storeName, contentType);
		return null;
	}

	// �ļ����� ��Ҫ����
	public static void download(HttpServletRequest request,
			HttpServletResponse response, String storeName, String contentType)
			throws Exception {

		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		// ��ȡ��Ŀ��Ŀ¼
		String ctxPath = request.getSession().getServletContext()
				.getRealPath("");

		// ��ȡ�����ļ�¶��
		String downLoadPath = ctxPath + "/uploadFile/" + storeName;

		// ��ȡ�ļ��ĳ���
		long fileLength = new File(downLoadPath).length();

		// �����ļ��������
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(storeName.getBytes("utf-8"), "ISO8859-1"));
		// �����������
		response.setHeader("Content-Length", String.valueOf(fileLength));
		// ��ȡ������
		bis = new BufferedInputStream(new FileInputStream(downLoadPath));
		// �����
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		// �ر���
		bis.close();
		bos.close();
	}

}