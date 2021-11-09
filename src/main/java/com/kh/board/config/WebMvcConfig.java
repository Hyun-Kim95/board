package com.kh.board.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	// 게시판의 첨부파일 저장 주소
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;

	// CORS 허용 : 다른 출처의 자원을 공류할 수 있도록 설정하는 권한 체제
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	// beforeActionInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("beforeActionInterceptor")
	HandlerInterceptor beforeActionInterceptor;

	// needLoginInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needLoginInterceptor")
	HandlerInterceptor needLoginInterceptor;

	// needAdminInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needAdminInterceptor")
	HandlerInterceptor needAdminInterceptor;
	
	// needLogoutInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needLogoutInterceptor")
	HandlerInterceptor needLogoutInterceptor;

	// 이 함수는 인터셉터를 적용하는 역할을 합니다.
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// beforeActionInterceptor 인터셉터가 모든 액션 실행전에 실행되도록 처리
		registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**")
				.excludePathPatterns("/gen/**");

		// 어드민 필요한 경우를 adm으로 시작하는 전체로 설정 후에 예외로 로그인 같은 것들 적어줌
		registry.addInterceptor(needAdminInterceptor).addPathPatterns("/adm/**")
				.excludePathPatterns("/adm/member/login").excludePathPatterns("/adm/member/doLogin")
				.excludePathPatterns("/adm/member/join").excludePathPatterns("/adm/member/doJoin")
				.excludePathPatterns("/adm/member/getLoginIdDup");

		// 로그인 필요한 경우를 전체로 설정 후 필요 없는 경우 설정
		registry.addInterceptor(needLoginInterceptor).addPathPatterns("/**").excludePathPatterns("/")
				.excludePathPatterns("/adm/**").excludePathPatterns("/gen/**").excludePathPatterns("/resource/**")
				.excludePathPatterns("/usr/member/authKey").excludePathPatterns("/usr/member/login")
				.excludePathPatterns("/usr/member/doLogin").excludePathPatterns("/usr/member/join")
				.excludePathPatterns("/usr/member/doJoin").excludePathPatterns("/usr/member/findLoginId")
				.excludePathPatterns("/usr/member/doFindLoginId").excludePathPatterns("/usr/member/findLoginPw")
				.excludePathPatterns("/usr/member/doFindLoginPw").excludePathPatterns("/common/**")
				.excludePathPatterns("/usr/file/test*").excludePathPatterns("/usr/file/doTest*")
				.excludePathPatterns("/test/**").excludePathPatterns("/error")
				.excludePathPatterns("/usr/member/getLoginIdDup");

		// 로그인 상태에서 접속할 수 없는 URI 전부 기술
		registry.addInterceptor(needLogoutInterceptor).addPathPatterns("/adm/member/login")
				.addPathPatterns("/adm/member/doLogin").addPathPatterns("/usr/member/login")
				.addPathPatterns("/usr/member/doLogin").addPathPatterns("/usr/member/join")
				.addPathPatterns("/usr/member/doJoin");
	}

	// 리소스 위치와 이 리소스와 매칭될 url을 등록
	// gen으로 오는 것들을 application.yml에 정해놓은 주소에 저장할거임
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/gen/**").addResourceLocations("file:///" + genFileDirPath + "/")
				.setCachePeriod(20);
	}
}
