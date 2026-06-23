package com.moa.backend.common.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.moa.backend.board.model.vo.Attachment;

@Component
public class FileUtil {

	@Value("${file.upload-path}")
	private String savePath;

	public Attachment saveFile(MultipartFile upload) {
		if (upload == null || upload.isEmpty()) {
			return null;
		}

		try {
			File folder = new File(savePath);
			if (!folder.exists()) // 폴더 없으면 만든다.
				folder.mkdirs();

			// 원본 파일 이름(확장자 포함)
			String originFileName = upload.getOriginalFilename();
			// 확장자
			String ext = originFileName.substring(originFileName.lastIndexOf("."));
			// rename 생성
			String renameFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
					+ (int) (Math.random() * 100000) + ext;
			// 저장
			upload.transferTo(new File(savePath + renameFileName));
			System.out.println("파일 저장 성공: " + renameFileName);

			Attachment attm = new Attachment();
			attm.setOriginalName(originFileName);
			attm.setRenameName(renameFileName);

			return attm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteFile(String renameName) {
		File f = new File(savePath + renameName);
		if (f.exists()) {
			f.delete();
			System.out.println("파일 삭제 완료: " + renameName);
		}
	}
}
