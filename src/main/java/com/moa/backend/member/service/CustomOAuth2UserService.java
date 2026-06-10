package com.moa.backend.member.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import com.moa.backend.member.service.MemberService;
import com.moa.backend.member.model.vo.Member;
import com.moa.backend.member.model.vo.MemberDetail;
@Service
@RequiredArgsConstructor

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

	private final MemberService mService;
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 기본 OAuth2UserService로 사용자 정보 가져오기
		DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		
		//카카오 응답에서 필요한 값 추출 
		Map<String, Object> attributes = oAuth2User.getAttributes();
		Long kakaoId = (Long) attributes.get("id");//카카오 고유 아이디 
		Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
		String nickname = (String) properties.get("nickname"); //카카오 닉네임 
		String profileImg = (String) properties.get("profile_image"); //카카오 프로필 이미지 
		
		//데이터베이스에서 카카오아이디로 기존 회원 조회 
		Member existMember = mService.findByKakaoId(kakaoId);
		
		if(existMember == null) {
			//신규회원 => member 테이블에 기본 정보 저장 
			Member newMember = new Member();
			newMember.setKakaoId(kakaoId.toString());
			newMember.setNickname(nickname);
			newMember.setLoginType("KAKAO");
			mService.insertMember(newMember);
			//신규회원 => member_detail 테이블에 상세 정보 저장 
			MemberDetail MD = new MemberDetail();
			MD.setMemberId(newMember.getMemberId());
			MD.setProfileImg(profileImg);
			mService.insertMemberDetail(MD);
		}else {
		//기존 회원은 별도 처리 없음 	
		}
		return oAuth2User;
	}
}
