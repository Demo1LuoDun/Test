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
	 * SpringMVC中的文件上传
	 * 
	 * @第一步：由于SpringMVC使用的是commons-fileupload实现，故将其组件引入项目中
	 * 
	 * @这里用到的是commons-fileupload-1.2.1.jar和commons-io-1.3.2.jar
	 * 
	 * @第二步：spring-mvx中配置MultipartResolver处理器。可在此加入对上传文件的属性限制 <bean
	 * id="multipartResolver"
	 * class="org.springframework.web.multipart.commons.CommonsMultipartResolver"
	 * > <!-- 设置上传文件的最大尺寸为10MB --> <property name="maxUploadSize">
	 * <value>10000000</value> </property> </bean>
	 * 第三步：在Controller的方法中添加MultipartFile参数。该参数用于接收表单中file组件的内容
	 * 第四步：编写前台表单。注意enctype="multipart/form-data"以及<input type="file"
	 * name="****"/> 如果是单个文件 直接使用MultipartFile 即可
	 */

	/********************** 上传代码 **************************/
	@RequestMapping("/upload.do")
	public ModelAndView upload(String name,@RequestParam("file") MultipartFile[] file,
			HttpServletRequest request) throws IllegalStateException,
			IOException {
		
		String realPath = request.getSession().getServletContext()
				.getRealPath("/uploadFile");

		File pathFile = new File(realPath);

		if (!pathFile.exists()) {
			// 文件夹不存 创建文件
			pathFile.mkdirs();
		}
		for (MultipartFile f : file) {
			if(f!=null && f.getSize()>0){
				System.out.println("文件类型：" + f.getContentType());
				System.out.println("文件名称：" + f.getOriginalFilename());
				System.out.println("文件大小:" + f.getSize());
				System.out.println(".................................................");
				// 将文件copy上传到服务器
				f.transferTo(new File(realPath + "/" + f.getOriginalFilename()));
				// FileUtils.copy
			}
		}
		// 获取modelandview对象
		ModelAndView view = new ModelAndView();
		view.setViewName("redirect:index.jsp");
		return view;
	}

	/******** 下载代码 *************/
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

	// 文件下载 主要方法
	public static void download(HttpServletRequest request,
			HttpServletResponse response, String storeName, String contentType)
			throws Exception {

		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		// 获取项目根目录
		String ctxPath = request.getSession().getServletContext()
				.getRealPath("");

		// 获取下载文件露肩
		String downLoadPath = ctxPath + "/uploadFile/" + storeName;

		// 获取文件的长度
		long fileLength = new File(downLoadPath).length();

		// 设置文件输出类型
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(storeName.getBytes("utf-8"), "ISO8859-1"));
		// 设置输出长度
		response.setHeader("Content-Length", String.valueOf(fileLength));
		// 获取输入流
		bis = new BufferedInputStream(new FileInputStream(downLoadPath));
		// 输出流
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		// 关闭流
		bis.close();
		bos.close();
	}

}