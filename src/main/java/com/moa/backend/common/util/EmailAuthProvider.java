package com.moa.backend.common.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailAuthProvider {
	
	private final JavaMailSender mailSender;

	@Value("${moa.secret.email-key}")
	private String SECRET_KEY;
	private final String ALGORITHM = "HmacSHA256";

	// 1. 6자리 인증번호 생성
	public String generateVerificationCode() {
		Random random = new Random();
		int code = 100000 + random.nextInt(900000); // 100000 ~ 999999 사이의 숫자
		return String.valueOf(code);
	}
	
	// 2. 인증번호를 담은 메일 전송
	public void sendAuthEmail(String email, String code) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		String subject = "[MOA] 회원가입 이메일 인증번호";

		String body = "<h1 align='center'>MOA 이메일 확인</h1><br/>";
		body += "<div style='border: 3px solid skyblue; text-align: center; font-size: 15px; padding: 20px;'>";
		body += "본 메일은 이메일을 확인하기 위해 발송되었습니다.<br/>";
		body += "아래 숫자를 인증번호 확인란에 작성하여 확인해주시기 바랍니다.<br/><br/>";
		body += "<span style='font-size: 30px; text-decoration: underline;'><b>" + code + "</b></span><br/></div>";
		
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		try {
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(body, true);
			
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	// 3. 이메일|인증번호|만료시간 으로 HMAC 토큰(해시) 만들기
	public String createHmacToken(String email, String code, long expireTime) {
		try {
			// 암호화할 원문 데이터 조합
			String rawData = email + "|" + code + "|" + expireTime;

			// HMAC-SHA256 엔진 초기화
			SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
			Mac mac;
			mac = Mac.getInstance(ALGORITHM);
			mac.init(secretKeySpec);

			// 해시값 계산 및 Base64 인코딩  
			byte[] hmacBytes = mac.doFinal(rawData.getBytes(StandardCharsets.UTF_8));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(hmacBytes);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException("HMAC 토큰 생성 중 암호화 에러 발생", e);
		}
	}

	// 4. 인증번호 검증
	public boolean verifyToken(String email, String inputCode, long expireTime, String clientToken) {
		// email 유저 이메일
		// inputCode 유저가 이메일로 받은 인증번호
		// expireTime 리액트에서 보낸 만료시간
		// clientToken 리액트에서 보낸 HMAC토큰
		if(System.currentTimeMillis() > expireTime) {return false;}
		String recalculatedToken = createHmacToken(email, inputCode, expireTime);
		return recalculatedToken.equals(clientToken);
	}
	
	//임시 비밀번호 생성
	public String generateTempPassword() {
		String charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*";
		SecureRandom secureRandom = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < 12; i++) {
			sb.append(charPool.charAt(secureRandom.nextInt(charPool.length())));
			
		}
		return sb.toString();
	}
	
	//임시비밀번호 초기화 인증 메일 발송
	public void sendTempPasswordEmail(String email, String tempPassword) {
	    MimeMessage mimeMessage = mailSender.createMimeMessage();
	    String subject = "[MOA] 임시 비밀번호 발급 안내";
	    String body = "<h1 align='center'>MOA 임시 비밀번호 안내</h1><br/>";
	    body += "<div style='border: 3px solid skyblue; text-align: center; font-size: 15px; padding: 20px;'>";
	    body += "임시 비밀번호가 발급되었습니다.<br/>";
	    body += "아래 임시 비밀번호로 로그인 후 반드시 비밀번호를 변경해주세요.<br/><br/>";
	    body += "<span style='font-size: 30px; text-decoration: underline;'><b>" + tempPassword + "</b></span><br/></div>";

	    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
	    try {
	        mimeMessageHelper.setTo(email);
	        mimeMessageHelper.setSubject(subject);
	        mimeMessageHelper.setText(body, true);
	        mailSender.send(mimeMessage);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	}
}




















