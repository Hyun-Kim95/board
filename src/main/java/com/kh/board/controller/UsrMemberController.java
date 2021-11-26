package com.kh.board.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.board.dto.Member;
import com.kh.board.service.MemberService;
import com.kh.board.util.Util;

@Controller
public class UsrMemberController extends BaseController{
	@Autowired
	private MemberService memberService;	
	
	@RequestMapping("/usr/member/modify")	// 본인 정보 수정 페이지
	public String showModify(HttpServletRequest req) {
		Member member = (Member)req.getAttribute("loginedMember");
		
		if (member == null) {
			return msgAndBack(req, "존재하지 않는 회원번호 입니다.");
		}
		
		req.setAttribute("member", member);

		return "usr/member/modify";
	}

	@RequestMapping("/usr/member/doModify")	// 정보 수정
	public String doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		// 로그인 비번이 공백으로 들어와도 null로 바꿔주기 위해				
		param.put("loginPw", Util.ifEmpty(param.get("loginPw"), null));
		
		memberService.modifyMember(param);
		
		return msgAndReplace(req, "회원정보가 수정되었습니다.", "../member/detail");
	}
	
	@RequestMapping("/usr/member/detail")
	public String showDetail(HttpServletRequest req) {
		Member member = (Member)req.getAttribute("loginedMember");
		
		req.setAttribute("member", member);

		return "usr/member/detail";
	}
}
