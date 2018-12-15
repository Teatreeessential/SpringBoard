package org.zerock.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {
	
	@Setter(onMethod_ = {@Autowired})
	private BoardAttachMapper attachMapper;
	
	private String getFolderYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DATE, -1);
		
		String str = sdf.format(cal.getTime());
		
		return str.replace("-", File.separator);
	}
	
	
	@Scheduled(cron="0 0 2 * * *")
	public void checkFiles()throws Exception{
		log.warn("File Check Task run......");
		log.warn(new Date());
		//실제 어제 저장된 파일의 값들을 전부 db에서 불러옴
		List<BoardAttachVO> list = attachMapper.getOldFiles();
		
		//db에서 저장된 값들 중 path만 필요하기 때문에 map을 통해서 path값만 가져옴
		List<Path> fileListPaths = list.stream()
				.map(vo -> Paths.get("c:\\upload",vo.getUploadPath(),vo.getUuid()+"_"+vo.getFileName()))
				.collect(Collectors.toList());
		
		//그중 filetype이 image일 경우 썸네일에 해당되는 부분도 가져와야하기때문
		list.stream()
		.filter(vo ->vo.isFileType() ==true)
		.map(vo -> Paths.get("c:\\upload",vo.getUploadPath(),"s_"+vo.getUuid()+"_"+vo.getFileName()))
		.forEach(p -> fileListPaths.add(p));
		
		//실제 어제 폴더에 존재하는 모든 파일들을 가져옴
		File targetDir = Paths.get("c:\\upload",getFolderYesterDay()).toFile();
		
		//list에 해당 경로가 존재하는지 존재한다면 아직 파일 삭제 안해도될것 존재안하면 파일 삭제할 것
		File[] removeFiles = targetDir.listFiles(file->fileListPaths.contains(file.toPath())==false);
		
		//삭제할 파일드릉ㄹ for each문을 통해 삭제
		for(File file: removeFiles) {
			log.warn(file.getAbsolutePath());
			file.delete();
		}
		
		
	}
}
