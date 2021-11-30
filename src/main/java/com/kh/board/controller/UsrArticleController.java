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
import com.kh.board.dto.Reply;
import com.kh.board.service.ArticleService;
import com.kh.board.service.BoardService;
import com.kh.board.service.GenFileService;
import com.kh.board.service.ReplyService;
import com.kh.board.util.Util;

@Controller
public class UsrArticleController extends BaseController{
	@Autowired
	private ArticleService articleService;
	@Autowired
	private GenFileService genFileService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private ReplyService replyService;

	@RequestMapping("/usr/article/detail")
	public String showDetail(@RequestParam Map<String, Object> param, HttpServletRequest req, Integer id) {
		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}
		// 게시물 정보		
		Article article = articleService.getForPrintArticle(id);
		// 게시판 정보(디테일에서 위에 게시판 이름보이게 하려고)
		Board board = boardService.getBoard(article.getBoardId());
		// 댓글 정보
		List<Reply> replies = replyService.getForPrintReplies(id);
		// 첨부파일 리스트
		List<GenFile> files = genFileService.getGenFiles(article.getId());

		Map<String, GenFile> filesMap = new HashMap<>();

		for (GenFile file : files) {
			// 삭제처리하지 않은 첨부파일만 put
			if(!file.isDelStatus())
				filesMap.put(file.getFileNo() + "", file);
		}
		
		article.getExtraNotNull().put("file", filesMap);
		// jsp에서 사용할 수 있도록 req에 추가
		req.setAttribute("article", article);
		req.setAttribute("board", board);
		req.setAttribute("replies", replies);
		
		return "usr/article/detail";
	}

	@RequestMapping("/usr/article/list")	// boardId의 기본값은 1(공지사항)으로 지정, page는 기본을 1페이지로 지정
	public String showList(HttpServletRequest req, @RequestParam(defaultValue = "1") int boardId,
			String searchKeywordType, String searchKeyword, @RequestParam(defaultValue = "1") int page) {

		// 현재 게시판 정보 전달(보여줄 게시판 선택용)
		Board board = articleService.getBoard(boardId);
		req.setAttribute("board", board);

		// 모든 게시판 정보 전달(select문에서 선택해서 이동해야 함)
		List<Board> boards = boardService.getBoards();
		req.setAttribute("boards", boards);

		if (board == null) {
			return msgAndBack(req, "존재하지 않는 게시판 입니다.");
		}

		// 검색할 타입(title or body)
		if (searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}
		// 검색창에 아무것도 검색 안하면 전체에서 검색으로 설정함
		if (searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "titleAndBody";
		}
		// 검색이 공백이어도 null로 변경
		if (searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}

		if (searchKeyword != null) {
			searchKeyword = searchKeyword.trim();
		}

		if (searchKeyword == null) {
			searchKeywordType = null;
		}

		// 페이징을 위해 게시물 수 확인
		int totalItemsCount = articleService.getArticlesTotalCount(boardId, searchKeywordType, searchKeyword);
		// 한 페이지에 보여줄 게시물 수
		int itemsInAPage = 20;
		// 전체 페이지 수
		int totalPage = (int) Math.ceil(totalItemsCount / (double) itemsInAPage);
		// (선택된 화면 외에 보여질)페이지 버튼 수
		int pageMenuArmSize = 4;
		// 페이지 버튼 가장 왼쪽 숫자
		int pageMenuStart = page - pageMenuArmSize;

		if (pageMenuStart < 1) {
			pageMenuStart = 1;
		}
		// 페이지 버튼 가장 오른쪽 숫자
		int pageMenuEnd = page + pageMenuArmSize;
		if (pageMenuEnd > totalPage) {
			pageMenuEnd = totalPage;
		}
		// 해당 페이지에 있는 게시물들
		List<Article> articles = articleService.getForPrintArticles(boardId, searchKeywordType, searchKeyword, page,
				itemsInAPage);
		// 뷰에서 사용함
		req.setAttribute("totalItemsCount", totalItemsCount);
		req.setAttribute("articles", articles);
		req.setAttribute("page", page);
		req.setAttribute("totalPage", totalPage);
		req.setAttribute("pageMenuArmSize", pageMenuArmSize);
		req.setAttribute("pageMenuStart", pageMenuStart);
		req.setAttribute("pageMenuEnd", pageMenuEnd);

		return "usr/article/list";
	}
	
	@RequestMapping("/usr/article/add")	// 게시물 추가 페이지로 이동
	public String showAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		return "usr/article/add";
	}

	@RequestMapping("/usr/article/doAdd")	// 실제 게시물 추가
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

	@RequestMapping("/usr/article/doDelete")
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

	@RequestMapping("/usr/article/modify")	// 게시물 수정 페이지로 이동
	public String showModify(Integer id, HttpServletRequest req) {
		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		Article article = articleService.getForPrintArticle(id);

		List<GenFile> files = genFileService.getGenFiles(article.getId());

		Map<String, GenFile> filesMap = new HashMap<>();

		for (GenFile file : files) {
			filesMap.put(file.getFileNo() + "", file);
		}

		article.getExtraNotNull().put("file", filesMap);
		req.setAttribute("article", article);

		return "usr/article/modify";
	}

	@RequestMapping("/usr/article/doModify")	// 실제 수정
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
		// 첨부파일 1~10까지 확인하여 삭제체크를 한 첨부파일이 있는지 확인
		for(int i=1;i<=10;i++) {
			// null을 toString 하면 오류 발생해서
			if(param.get("deleteFile__"+param.get("id")+"__"+i) != null && param.get("deleteFile__"+param.get("id")+"__"+i).toString().equals("Y")) {
				GenFile gen = genFileService.getGenFile(Integer.parseInt(param.get("id").toString()), i);
				// 해당파일의 delStatus를 1로 변경
				genFileService.changeDeleteFileById(gen);
			}
		}
		
		int newArticleId = articleService.modifyArticle(param);
		
		return msgAndReplace(req, String.format("%d번 게시물이 수정되었습니다.", newArticleId),
				"../article/detail?id=" + newArticleId);
	}
}
