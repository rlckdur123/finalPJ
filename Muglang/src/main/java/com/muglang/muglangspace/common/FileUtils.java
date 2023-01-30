package com.muglang.muglangspace.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.muglang.muglangspace.entity.MglgPostFile;
import com.muglang.muglangspace.entity.MglgUserProfile;


public class FileUtils {
	//Map<String, String> => 파일 업로드 기능이 여러군데에서 사용될 때 범용성을 높이기 위해
	//Map을 사용한다. Map을 사용할 경우 매퍼까지 Map으로 보내준다.
	public static MglgPostFile parseFileInfo(MultipartFile file, 
			String attachPath) throws IOException {
		MglgPostFile postFile = new MglgPostFile();
		
		String postFileOriginNm = file.getOriginalFilename();
		
		//같은 파일명을 업로드했을 때 덮어써지지 않게 하기위한 실제 업로드되는 파일명 설정
		SimpleDateFormat formmater = new SimpleDateFormat("yyyyMMddHHmmss");
		Date nowDate = new Date();
		String nowDateStr = formmater.format(nowDate);
		UUID uuid = UUID.randomUUID();
		
		System.out.println("222222222222222222222222222222222222222222222");
		
		String postFileNm = nowDateStr + "_" + uuid.toString() + "_" + postFileOriginNm;
		
		String postFilePath = attachPath;
		
		//이미지인지 다른 파일형태인지 검사
		File checkFile = new File(postFileOriginNm);
		//업로드한 파일의 형식 가져옴(이미지파일들은 image/jpg, image/png ...)
		String type = Files.probeContentType(checkFile.toPath());
		System.out.println("파일의 형식 : " + type);
		
		if(type != null) {
			if(type.startsWith("image")) {
				postFile.setPostFileCate("img");
			} else {
				postFile.setPostFileCate("etc");
			}
		} else {
			postFile.setPostFileCate("etc");
		}
		
		postFile.setPostFileNm(postFileNm);
		postFile.setPostFileOriginNm(postFileOriginNm);
		postFile.setPostFilePath(postFilePath);

		//실제 파일 업로드
		File uploadFile = new File(attachPath + postFileNm);
		//매개변수는 업로드될 폴더와 파일명을 파일객체 형태로 넣어준다.
		//파일업로드 시 IOException 처리
		file.transferTo(uploadFile);
		
		return postFile;
	}
	public static MglgUserProfile parseProfileInfo(MultipartFile file, 
			String attachPath) throws IOException {
		MglgUserProfile profileFile = new MglgUserProfile();
		
		String profileFileOriginNm = file.getOriginalFilename();
		
		//같은 파일명을 업로드했을 때 덮어써지지 않게 하기위한 실제 업로드되는 파일명 설정
		SimpleDateFormat formmater = new SimpleDateFormat("yyyyMMddHHmmss");
		Date nowDate = new Date();
		String nowDateStr = formmater.format(nowDate);
		UUID uuid = UUID.randomUUID();
		
		System.out.println("222222222222222222222222222222222222222222222");
		
		String profileFileNm = nowDateStr + "_" + uuid.toString() + "_" + profileFileOriginNm;
		
		String profileFilePath = attachPath;
		
		//이미지인지 다른 파일형태인지 검사
		File checkFile = new File(profileFileOriginNm);
		//업로드한 파일의 형식 가져옴(이미지파일들은 image/jpg, image/png ...)
		String type = Files.probeContentType(checkFile.toPath());
		System.out.println("파일의 형식 : " + type);
		
		if(type != null) {
			if(type.startsWith("image")) {
				profileFile.setUserProfileCate("img");
			} else {
				profileFile.setUserProfileCate("etc");
			}
		} else {
			profileFile.setUserProfileCate("etc");
		}
		
		profileFile.setUserProfileNm(profileFileNm);
		profileFile.setUserProfileOriginNm(profileFileOriginNm);
		profileFile.setUserProfilePath(profileFilePath);

		//실제 파일 업로드
		File uploadFile = new File(attachPath + profileFileNm);
		//매개변수는 업로드될 폴더와 파일명을 파일객체 형태로 넣어준다.
		//파일업로드 시 IOException 처리
		file.transferTo(uploadFile);
		
		return profileFile;
	}
}
