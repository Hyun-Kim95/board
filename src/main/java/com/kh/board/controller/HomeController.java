package com.kh.board.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.board.dto.Member;
import com.kh.board.dto.ResultData;
import com.kh.board.service.MemberService;
import com.kh.board.util.Util;
// 로그인, 회원가입, 메인페이지 등을 관리
@Controller
public class HomeController {
	@Autowired
	private MemberService memberService;
	
	@GetMapping(value = {"/usr/member/getLoginIdDup","/adm/member/getLoginIdDup"})
	@ResponseBody
	public ResultData getLoginIdDup(String loginId) {			// 회원가입 시 확인할 것들
		if (loginId == null) {
			return new ResultData("F-5", "loginId를 입력해주세요.");
		}

		if (Util.allNumberString(loginId)) {
			return new ResultData("F-3", "로그인아이디는 숫자만으로 구성될 수 없습니다.");
		}

		if (Util.startsWithNumberString(loginId)) {
			return new ResultData("F-4", "로그인아이디는 숫자로 시작할 수 없습니다.");
		}

		if (loginId.length() < 5) {
			return new ResultData("F-5", "로그인아이디는 5자 이상으로 입력해주세요.");
		}

		if (loginId.length() > 20) {
			return new ResultData("F-6", "로그인아이디는 20자 이하로 입력해주세요.");
		}

		if (Util.isStandardLoginIdString(loginId) == false) {
			return new ResultData("F-1", "로그인아이디는 영문소문자와 숫자의 조합으로 구성되어야 합니다.");
		}

		Member existingMember = memberService.getMemberByLoginId(loginId);

		if (existingMember != null) {
			return new ResultData("F-2", String.format("%s(은)는 이미 사용중인 로그인아이디 입니다.", loginId));
		}

		return new ResultData("S-1", String.format("%s(은)는 사용가능한 로그인아이디 입니다.", loginId), "loginId", loginId);
	}
	
	@RequestMapping(value = {"/adm/member/login", "/usr/member/login"})
	public String showLogin() {
		// 관리자든 유저페이지든 유저페이지로그인 화면으로 이동
		return "usr/member/login";
	}

	@RequestMapping(value = {"/adm/member/doLogin", "/usr/member/doLogin"})
	@ResponseBody
	public String doLogin(String loginId, String loginPw, String redirectUrl, HttpSession session) {
		if (loginId == null) {
			return Util.msgAndBack("loginId를 입력해주세요.");
		}

		Member existingMember = memberService.getMemberByLoginId(loginId);

		if (existingMember == null) {
			return Util.msgAndBack("존재하지 않는 로그인아이디 입니다.");
		}

		if (loginPw == null) {
			return Util.msgAndBack("loginPw를 입력해주세요.");
		}

		if (existingMember.getLoginPw().equals(loginPw) == false) {
			return Util.msgAndBack("비밀번호가 일치하지 않습니다.");
		}

		session.setAttribute("loginedMemberId", existingMember.getId());

		String msg = String.format("%s님 환영합니다.", existingMember.getNickname());
		// 이동할 주소가 없으면 유저메인페이지로 이동
		redirectUrl = Util.ifEmpty(redirectUrl, "../../usr/home/main");

		return Util.msgAndReplace(msg, redirectUrl);
	}
	
	@RequestMapping("/adm/home/main")
	public String showMain1() {
		return "adm/home/main";
	}
	
	@RequestMapping("/usr/home/main")
	public String showMain2() {
		return "usr/home/main";
	}
	
	@RequestMapping(value = {"/adm/member/join", "/usr/member/join"})
	public String showJoin() {
		return "usr/member/join";
	}

	@RequestMapping(value = {"/adm/member/doJoin", "/usr/member/doJoin"})
	@ResponseBody
	public String doJoin(@RequestParam Map<String, Object> param) {
		if (param.get("loginId") == null) {
			return Util.msgAndBack("loginId를 입력해주세요.");
		}

		Member existingMember = memberService.getMemberByLoginId((String) param.get("loginId"));
		// 이미 가입된 아이디 인지와 공백인지 확인
		if (existingMember != null) {
			return Util.msgAndBack("이미 사용중인 로그인아이디 입니다.");
		}

		if (param.get("loginPw") == null) {
			return Util.msgAndBack("loginPw를 입력해주세요.");
		}

		if (param.get("name") == null) {
			return Util.msgAndBack("name을 입력해주세요.");
		}

		if (param.get("nickname") == null) {
			return Util.msgAndBack("nickname을 입력해주세요.");
		}

		if (param.get("email") == null) {
			return Util.msgAndBack("email을 입력해주세요.");
		}

		if (param.get("cellphoneNo") == null) {
			return Util.msgAndBack("cellphoneNo를 입력해주세요.");
		}

		memberService.join(param);

		String msg = String.format("%s님 환영합니다.", param.get("nickname"));

		String redirectUrl = Util.ifEmpty((String) param.get("redirectUrl"), "../member/login");

		return Util.msgAndReplace(msg, redirectUrl);
	}
	
	@RequestMapping(value = {"/usr/member/doLogout", "/adm/member/doLogout"})
	@ResponseBody
	public String doLogout(HttpSession session) {
		// 세션에서 로그인정보를 삭제
		session.removeAttribute("loginedMemberId");

		return Util.msgAndReplace("로그아웃 되었습니다.", "../member/login");
	}
}
