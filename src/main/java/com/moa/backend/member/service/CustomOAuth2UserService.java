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
@Service
@RequiredArgsConstructor

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

	private final MemberService mService;
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 기본 OAuth2UserService로 사용자 정보 가져오기
		DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		
		Map<String, Object> attributes = oAuth2User.getAttributes();
		Long kakaoId = (Long) attributes.get("id");
		Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
		String nickname = (String) properties.get("nickname");
		String profileImg = (String) properties.get("profile_image");
		Member existMember = mService.findByKakaoId(kakaoId);
		
		if(existMember == null) {
			Member newMember = new Member();
			mService.insertMember(newMember);
			newMember.setKakaoId(kakaoId.toString());
			newMember.setNickname(nickname);
			newMember.setLoginType("KAKAO");
			
			
		}else {
			
		}
		return oAuth2User;
	}
}
