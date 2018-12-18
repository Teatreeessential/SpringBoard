package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.BoardAttachVO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class FileController {
	
	static final String uploadRootPath = "c:\\upload\\";
	
	public String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();

		String str = sdf.format(date);
		return str.replace("-", File.separator);

	}
	public boolean checkFileImage(File file) {
		try {
			System.out.println(file);
			String contentType = Files.probeContentType(file.toPath());
			
			if(contentType.startsWith("image")) {
				return true;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false; 
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> uploadAjaxAction(MultipartFile[] uploadFile) {
		List<BoardAttachVO> list = new ArrayList<>();
		File file = new File(uploadRootPath,getFolder());
		
		if(!file.exists()) {
			file.mkdirs();
		}
		
		for(MultipartFile multiFile: uploadFile) {
			BoardAttachVO vo= new BoardAttachVO();
			UUID uuid = UUID.randomUUID();
			vo.setFileName(multiFile.getOriginalFilename());
			vo.setUploadPath(getFolder());
			vo.setUuid(uuid.toString());
			
			
			file = new File(uploadRootPath+vo.getUploadPath()+"\\"+vo.getUuid()+"_"+vo.getFileName());
			System.out.println("파일 저장소");
			System.out.println(file.toString());
			
			try {
				multiFile.transferTo(file);
				if(checkFileImage(file)) {
					vo.setFileType(true);
					file = new File(uploadRootPath+vo.getUploadPath()+"\\s_"+vo.getUuid()+"_"+vo.getFileName());
					FileOutputStream thumbnail = new FileOutputStream(file);
					Thumbnailator.createThumbnail(multiFile.getInputStream(),thumbnail,200,200);
					thumbnail.close();
				}
				
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			list.add(vo);
		}
		return new ResponseEntity<List<BoardAttachVO>>(list,HttpStatus.OK);
	}
	
	@GetMapping(value="/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		
		log.info("fileName:"+fileName);
		
		File file = new File("C:\\upload\\"+fileName);
		
		log.info("file:" + file);
		
		ResponseEntity<byte[]> result = null;
		
		
		
		try {
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@PostMapping(value="/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName,String type){
		try {
			File file = new File(uploadRootPath+URLDecoder.decode(fileName,"UTF-8"));
			file.delete();
			if(type.equals("image")) {
				
				file = new File(uploadRootPath+URLDecoder.decode(fileName,"UTF-8").replace("s_", ""));
				file.delete();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<String>("file delete",HttpStatus.OK);
	}
	
	@GetMapping(value="/download",produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(String fileName){
		log.info("download file:" + fileName);
		fileName = fileName.replace("s_", "");
		Resource resource = new FileSystemResource("c:\\upload\\"+fileName);
		log.info("resource:"+resource);
		
		String resourceName = resource.getFilename();
				resourceName  = resourceName.substring(resourceName.lastIndexOf("_")+1);		
		HttpHeaders headers = new HttpHeaders();
		
		try {
			headers.add("Content-Disposition", "attachment; filename="+new String(resourceName.getBytes("UTF-8"),"ISO-8859-1"));
			
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
	}
}
