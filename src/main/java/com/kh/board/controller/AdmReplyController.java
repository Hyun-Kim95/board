package com.kh.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.board.dto.Reply;
import com.kh.board.dto.ResultData;
import com.kh.board.service.ReplyService;

@Controller
public class AdmReplyController extends BaseController {
	@Autowired
	private ReplyService replyService;
	
	@RequestMapping("adm/reply/add") //댓글 작성
    public String addReply(@RequestParam int articleId, @RequestParam String body, HttpServletRequest req) {
        int memberId = (int)req.getAttribute("loginedMemberId");
        replyService.addReply(articleId, memberId, body);
        
        return msgAndReplace(req, "댓글이 작성되었습니다.",
				"../article/detail?id=" + articleId);
    }

	@RequestMapping("/adm/reply/list")
	@ResponseBody
	public ResultData showList(Integer articleId) {

		if (articleId == null) {
			return new ResultData("F-1", "articleId를 입력해주세요.");
		}
		
		List<Reply> replies = replyService.getForPrintReplies(articleId);

		return new ResultData("S-1", "성공", "replies", replies);
	}

	@RequestMapping("/adm/reply/doDelete")
	public String doDelete(Integer id, HttpServletRequest req) {
		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		Reply reply = replyService.getReply(id);

		if (reply == null) {
			return msgAndBack(req, "해당 댓글은 존재하지 않습니다.");
		}
		int articleId = reply.getArticleId();
		
		replyService.deleteReply(id);
		
		return msgAndReplace(req, "댓글이 삭제되었습니다.",
				"../article/detail?id=" + articleId);
	}
	
	@RequestMapping("/adm/reply/doModify")
	public String doModify(Integer id, String body_modify, HttpServletRequest req) {
		
		if (body_modify == null) {
			return msgAndBack(req, "수정할 내용을 입력해주세요.");
		}

		Reply reply = replyService.getReply(id);

		if (reply == null) {
			return msgAndBack(req, "해당 댓글은 존재하지 않습니다.");
		}
		
		int articleId = reply.getArticleId();
		
		if (reply.getBody() == body_modify) {
			return "../article/detail?id="+articleId;
		}
		
		replyService.modifyReply(id, body_modify);
		
		return msgAndReplace(req, "댓글이 수정되었습니다.",
				"../article/detail?id=" + articleId);
	}
}