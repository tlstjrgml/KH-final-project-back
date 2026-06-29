package com.moa.backend.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3UploadService {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	private final S3Client s3Client;
	
	public S3UploadService(S3Client s3Client) {
		this.s3Client = s3Client;
	}
	
	public String uploadFile(MultipartFile file) throws IOException{
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
		
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucket)
				.key(fileName)
				.contentType(file.getContentType())
				.build();
		
		s3Client.putObject(putObjectRequest,
				RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
		
		return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
			
	}
	
	public void deleteFile(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucket)
				.key(fileName)
				.build();
		
		s3Client.deleteObject(deleteObjectRequest);
	}
}
