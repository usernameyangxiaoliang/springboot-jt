package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;

@Controller
public class FileController {
	@Autowired
	private FileService fileService;
	/**
	 * 1. 获取用户文件信息，包含文件名称
	 * 2.指定文件上传路径
	 * 3.实现文件的上传
	 * @param fileImage
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws IllegalStateException, IOException {
		//1.获取文件名称input标签中的name属性
		String inputName = fileImage.getName();
		System.out.println("1:"+inputName);
		//2.获取文件的名称
		String FileName = fileImage.getOriginalFilename();
		
		//3.定义文件夹路径
		File fileDir = new File("D:/jt/image");
		
		if(!fileDir.exists()) {
			//创建文件
			fileDir.mkdirs();
		}
		
		//4.实现文件上传
		fileImage.transferTo(new File("D:/jt/image/"+FileName));
		
		return "redirect:/file.jsp";
		
	}
	
	
	//实现文件上传
	@RequestMapping("/pic/upload")
	@ResponseBody
	public ImageVO uploadFile(MultipartFile uploadFile) {
		return fileService.uploadFile(uploadFile);
	}
	
	
	
	
	
	
	
	
	
	
	

}
