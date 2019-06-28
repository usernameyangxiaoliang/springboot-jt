package com.jt.service;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.jt.vo.ImageVO;
@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService{
	//定义本地磁盘路径
	@Value("${image.localDirPath}")
	private String localDirPath;
	//定义虚拟磁盘路径
	@Value("${image.urlPath}")
	private String urlPath;
	@Override
	public ImageVO uploadFile(MultipartFile uploadFile) {
		/**
		 * 1.获取图片名称
		 * 2.校验是否为图片类型
		 * 3.校验是否为恶意程序木马.exe.jsp
		 * 4.分文件保存，按照时间存储 
		 * 5.防止文件重名 UUID 32位16进制数+毫秒数
		 * 6.正则常用字符
		 * 1.^  标识以。。。开始
		 * 2.$  标识以...结束
		 * 3.点.  任意单个字符
		 * 4.*   标识0-无穷
		 * 5.+  标识1-无穷
		 * 6.
		 * 
		 */
		ImageVO imageVO = new ImageVO();
		//1.获取图片名称
		String fileName = uploadFile.getOriginalFilename();
		fileName=fileName.toLowerCase();
		//2.校验是否为图片类型 使用正则表达式
		if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {
			imageVO.setError(1);
			return imageVO;
		}
		//3.校验是否为恶意程序木马.exe.jsp
		try {
			BufferedImage bufferedImage= ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			if(width==0 || height==0) {
				imageVO.setError(1);
				return imageVO;
			}
			//4.时间转化为字符串
			String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			
			//5.准备文件夹
			String localDir = localDirPath+dateDir;
			File dirFile = new File(localDir);
			if(!dirFile.exists()) {
				//如果文件不存在则创建文件
				dirFile.mkdirs();
			}
			//6.使用UUID定义文件名称 replace替换
			String uuid=UUID.randomUUID().toString().replace("-","");
			//图片类型a.jsp动态获取".jsp" substring 截取
			String fileType=fileName.substring(fileName.lastIndexOf("."));
			
			//拼接新的文件名字
			String realLocalPath = localDir+"/"+uuid+fileType;
			
			//7.完成文件的上传
			uploadFile.transferTo(new File(realLocalPath));
			//8.拼接虚拟url路径
			String realUrlPath=urlPath+dateDir+"/"+uuid+fileType;
			//将文件信息回传给页面
			imageVO.setError(0).setHeight(height).setWidth(width).setUrl(realUrlPath);
		} catch (IOException e) {
			e.printStackTrace();
			imageVO.setError(1);
		}
		
		return imageVO;
	}
	
	

}
