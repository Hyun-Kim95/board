package com.kh.board.controller;

import java.util.List;
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
public class AdmMemberController extends BaseController{
	@Autowired
	private MemberService memberService;


	@RequestMapping("/adm/member/list")
	public String showList(HttpServletRequest req, @RequestParam(defaultValue = "1") int page,
			@RequestParam Map<String, Object> param) {
		
		int itemsInAPage = 20;

		List<Member> members = memberService.getForPrintMembers(null, null, page, itemsInAPage, param);

		req.setAttribute("members", members);

		return "adm/member/list";
	}

	@RequestMapping("/adm/member/modify")
	public String showModify(Integer id, HttpServletRequest req) {
		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		Member member = memberService.getForPrintMember(id);

		req.setAttribute("member", member);

		if (member == null) {
			return msgAndBack(req, "존재하지 않는 회원번호 입니다.");
		}

		return "adm/member/modify";
	}

	@RequestMapping("/adm/member/doModify")
	public String doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		
		memberService.modifyMember(param);
		
		return msgAndReplace(req, "회원정보가 수정되었습니다.", "../member/list");
	}
	
	@RequestMapping("/adm/member/detail")
	public String showDetail(HttpServletRequest req, @RequestParam Map<String, Object> param) {

		Member member = memberService.getForPrintMember(Util.getAsInt(param.get("id"),0));
		if (member == null) {
			return msgAndBack(req, "존재하지 않는 회원 입니다.");
		}
		req.setAttribute("member", member);

		return "adm/member/detail";
	}
	
	@RequestMapping("/adm/member/doDelete")
	public String doDelete(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		
		Member member = memberService.getMember(Util.getAsInt(param.get("id"),0));

		if (member == null) {
			return msgAndBack(req, "존재하지 않는 회원입니다.");
		}

		memberService.deleteMember(Util.getAsInt(param.get("id"),0));
		
		return msgAndReplace(req, "회원정보가 삭제되었습니다.", "../member/list");
	}
}
