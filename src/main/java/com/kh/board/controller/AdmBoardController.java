package com.kh.board.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartRequest;

import com.kh.board.dto.Article;
import com.kh.board.dto.Board;
import com.kh.board.dto.ResultData;
import com.kh.board.service.ArticleService;
import com.kh.board.service.BoardService;
import com.kh.board.util.Util;

@Controller
public class AdmBoardController extends BaseController{
	@Autowired
	private BoardService boardService;
	@Autowired
	private ArticleService articleService;

	@RequestMapping("/adm/board/list")
	public String showList(HttpServletRequest req) {

		List<Board> boards = boardService.getBoards();

		req.setAttribute("boards", boards);

		return "adm/board/list";
	}

	@RequestMapping("/adm/board/add")
	public String showAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		return "adm/board/add";
	}

	@RequestMapping("/adm/board/doAdd")
	public String doAdd(@RequestParam Map<String, Object> param, HttpServletRequest req,
			MultipartRequest multipartRequest) {

		if (param.get("code") == null) {
			return msgAndBack(req, "code를 입력해주세요.");
		}
		
		if (param.get("name") == null) {
			return msgAndBack(req, "name을 입력해주세요.");
		}

		ResultData addBoardRd = boardService.addBoard(param);

		int newBoardId = (int) addBoardRd.getBody().get("id");

		return msgAndReplace(req, String.format("%d번 게시판이 생성되었습니다.", newBoardId), "../board/list");
	}

	@RequestMapping("/adm/board/doDelete")
	public String doDelete(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		int id = (int)Util.getAsInt(param.get("id"), 0);
		if (id == 0) {
			return msgAndBack(req, "id를 입력해주세요.");
		}
		Board board = boardService.getBoard(id);
		List<Article> article = articleService.getArticlesBoardId(id);

		if (board == null) {
			return msgAndBack(req, "해당 게시판은 존재하지 않습니다.");
		}
		for (Article art : article) {
			articleService.deleteArticle(art.getId());
		}
		boardService.deleteBoard(id);
		
		return msgAndReplace(req, String.format("%d번 게시판이 삭제되었습니다.", id), "../board/list");
	}

	@RequestMapping("/adm/board/modify")
	public String showModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {

		Board board = boardService.getBoard(Util.getAsInt(param.get("id"), 0));
		
		if (board == null) {
			return msgAndBack(req, "존재하지 않는 게시판입니다.");
		}

		return "adm/board/modify";
	}

	@RequestMapping("/adm/board/doModify")
	public String doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		int id = Util.getAsInt(param.get("boardId"), 0);
		if (id == 0) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		boardService.modifyBoard(param);

		return msgAndReplace(req, String.format("%d번 게시판이 수정되었습니다.", id), "../board/list");
	}
}
