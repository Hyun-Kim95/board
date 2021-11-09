package com.kh.board.controller;

import java.util.HashMap;
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
import com.kh.board.dto.GenFile;
import com.kh.board.service.ArticleService;
import com.kh.board.service.BoardService;
import com.kh.board.service.GenFileService;
import com.kh.board.util.Util;

@Controller
public class AdmArticleController extends BaseController{
	@Autowired
	private ArticleService articleService;
	@Autowired
	private GenFileService genFileService;
	@Autowired
	private BoardService boardService;

	@RequestMapping("/adm/article/detail")
	public String showDetail(HttpServletRequest req, Integer id) {
		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}
		// 게시물 정보
		Article article = articleService.getForPrintArticle(id);
		// 게시판 정보(디테일에서 위에 게시판 이름보이게 하려고)
		Board board = boardService.getBoard(article.getBoardId());
		
		// 첨부파일 리스트
		List<GenFile> files = genFileService.getGenFiles("article", article.getId(), "common", "attachment");
		Map<String, GenFile> filesMap = new HashMap<>();
		
		for (GenFile file : files) {
			filesMap.put(file.getFileNo() + "", file);
		}

		article.getExtraNotNull().put("file__common__attachment", filesMap);
		// jsp에서 사용할 수 있도록 req에 추가
		req.setAttribute("article", article);
		req.setAttribute("board", board);

		return "adm/article/detail";
	}

	@RequestMapping("/adm/article/list")
	public String showList(HttpServletRequest req, @RequestParam(defaultValue = "1") int boardId,
			String searchKeywordType, String searchKeyword, @RequestParam(defaultValue = "1") int page) {
		
		// 현재 게시판 정보 전달
		Board board = articleService.getBoard(boardId);
		req.setAttribute("board", board);
		
		// 모든 게시판 정보 전달
		List<Board> boards = boardService.getBoards();
		req.setAttribute("boards", boards);

		if (board == null) {
			return msgAndBack(req, "존재하지 않는 게시판 입니다.");
		}

		if (searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}
		// 검색창에 아무것도 검색 안하면 전체에서 검색으로 설정함
		if (searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "titleAndBody";
		}

		if (searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}

		if (searchKeyword != null) {
			searchKeyword = searchKeyword.trim();
		}

		if (searchKeyword == null) {
			searchKeywordType = null;
		}

		int totalItemsCount = articleService.getArticlesTotalCount(boardId, searchKeywordType, searchKeyword);

		int itemsInAPage = 20;
		int totalPage = (int) Math.ceil(totalItemsCount / (double) itemsInAPage);
		int pageMenuArmSize = 10;
		int pageMenuStart = page - pageMenuArmSize;

		if (pageMenuStart < 1) {
			pageMenuStart = 1;
		}

		int pageMenuEnd = page + pageMenuArmSize;
		if (pageMenuEnd > totalPage) {
			pageMenuEnd = totalPage;
		}

		List<Article> articles = articleService.getForPrintArticles(boardId, searchKeywordType, searchKeyword, page,
				itemsInAPage);

		req.setAttribute("totalItemsCount", totalItemsCount);
		req.setAttribute("articles", articles);
		req.setAttribute("page", page);
		req.setAttribute("totalPage", totalPage);
		req.setAttribute("pageMenuArmSize", pageMenuArmSize);
		req.setAttribute("pageMenuStart", pageMenuStart);
		req.setAttribute("pageMenuEnd", pageMenuEnd);

		return "adm/article/list";
	}
	
	@RequestMapping("/adm/article/add")
	public String showAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		return "adm/article/add";
	}

	@RequestMapping("/adm/article/doAdd")
	public String doAdd(@RequestParam Map<String, Object> param, HttpServletRequest req,
			MultipartRequest multipartRequest) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		if (param.get("title") == null) {
			return msgAndBack(req, "title을 입력해주세요.");
		}

		if (param.get("body") == null) {
			return msgAndBack(req, "body를 입력해주세요.");
		}

		param.put("memberId", loginedMemberId);

		int newArticleId = articleService.addArticle(param);

		return msgAndReplace(req, String.format("%d번 게시물이 작성되었습니다.", newArticleId),
				"../article/detail?id=" + newArticleId);
	}

	@RequestMapping("/adm/article/doDelete")
	public String doDelete(Integer id, HttpServletRequest req) {

		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		Article article = articleService.getArticle(id);

		if (article == null) {
			return msgAndBack(req, "해당 게시물은 존재하지 않습니다.");
		}

		articleService.deleteArticle(id);
		
		return msgAndReplace(req, "게시물이 삭제되었습니다.", "../article/list");
	}

	@RequestMapping("/adm/article/modify")
	public String showModify(Integer id, HttpServletRequest req) {
		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		Article article = articleService.getForPrintArticle(id);

		List<GenFile> files = genFileService.getGenFiles("article", article.getId(), "common", "attachment");

		Map<String, GenFile> filesMap = new HashMap<>();

		for (GenFile file : files) {
			filesMap.put(file.getFileNo() + "", file);
		}

		article.getExtraNotNull().put("file__common__attachment", filesMap);
		req.setAttribute("article", article);

		return "adm/article/modify";
	}

	@RequestMapping("/adm/article/doModify")
	public String doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {

		int id = Util.getAsInt(param.get("id"), 0);

		if (id == 0) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		if (Util.isEmpty(param.get("title"))) {
			return msgAndBack(req, "title을 입력해주세요.");
		}

		if (Util.isEmpty(param.get("body"))) {
			return msgAndBack(req, "body를 입력해주세요.");
		}

		Article article = articleService.getArticle(id);

		if (article == null) {
			return msgAndBack(req, "해당 게시물은 존재하지 않습니다.");
		}
		
		int newArticleId = articleService.modifyArticle(param);
		
		return msgAndReplace(req, String.format("%d번 게시물이 수정되었습니다.", newArticleId),
				"../article/detail?id=" + newArticleId);
	}
}
