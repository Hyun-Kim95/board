package com.kh.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.board.dto.Article;
import com.kh.board.dto.Board;
import com.kh.board.dto.GenFile;
import com.kh.board.service.ArticleService;
import com.kh.board.service.BoardService;
import com.kh.board.service.GenFileService;
import com.kh.board.util.Util;

@Controller
public class AdmDeleteFileController extends BaseController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private GenFileService genFileService;
	@Autowired
	private BoardService boardService;

	@RequestMapping("/adm/delArticle/detail")
	public String showDelDetail(HttpServletRequest req, Integer id) {
		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}
		// 게시물 정보
		Article article = articleService.getForPrintArticle(id);
		// 게시판 정보(디테일에서 위에 게시판 이름보이게 하려고)
		Board board = boardService.getBoard(article.getBoardId());

		// 첨부파일 리스트
		List<GenFile> files = genFileService.getGenFiles(article.getId());
		
		Map<String, GenFile> filesMap = new HashMap<>();

		for (GenFile file : files) {
			filesMap.put(file.getFileNo() + "", file);
		}
		
		article.getExtraNotNull().put("file", filesMap);
		// jsp에서 사용할 수 있도록 req에 추가
		req.setAttribute("article", article);
		req.setAttribute("board", board);

		return "adm/delArticle/detail";
	}

	@RequestMapping("/adm/delArticle/list") // boardId의 기본값은 1(공지사항)으로 지정, page는 기본을 1페이지로 지정
	public String showDelList(HttpServletRequest req, @RequestParam(defaultValue = "1") int boardId,
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
		int totalItemsCount = articleService.getArticlesTotalCountByDel(boardId, searchKeywordType, searchKeyword);
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

		return "adm/delArticle/list";
	}
	
	@RequestMapping("/adm/delArticle/genList")
	public String showGenList(HttpServletRequest req, @RequestParam(defaultValue = "1") int page) {

		// 모든 게시판 정보 전달(select문에서 선택해서 이동해야 함)
		List<Board> boards = boardService.getBoards();
		req.setAttribute("boards", boards);
				
		// 페이징을 위해 첨부파일 수 확인
		int totalItemsCount = genFileService.getGenFilesTotalCountByDel();
		// 한 페이지에 보여줄 게시물 수
		int itemsInAPage = 10;
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
		List<GenFile> genFiles = genFileService.getForPrintGenFilesByDel(page, itemsInAPage);
		
		// 뷰에서 사용함
		req.setAttribute("totalItemsCount", totalItemsCount);
		req.setAttribute("genFiles", genFiles);
		req.setAttribute("page", page);
		req.setAttribute("totalPage", totalPage);
		req.setAttribute("pageMenuArmSize", pageMenuArmSize);
		req.setAttribute("pageMenuStart", pageMenuStart);
		req.setAttribute("pageMenuEnd", pageMenuEnd);
		
		return "adm/delArticle/genList";
	}
	
	@RequestMapping("/adm/delArticle/deleteCompletely")
	public String deleteCompletely(Integer id, HttpServletRequest req) {

		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		Article article = articleService.getArticle(id);

		if (article == null) {
			return msgAndBack(req, "해당 게시물은 존재하지 않습니다.");
		}

		articleService.deleteCompletelyArticle(id);
		
		return msgAndReplace(req, "게시물이 완전삭제되었습니다.", "../delArticle/list");
	}
	
	@RequestMapping("/adm/delArticle/deleteGenCompletely")
	public String deleteGenCompletely(Integer id, HttpServletRequest req) {

		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		GenFile genfile = genFileService.getGenFile(id);

		if (genfile == null) {
			return msgAndBack(req, "해당 파일은 존재하지 않습니다.");
		}

		genFileService.deleteGenFile(genfile);
		
		return msgAndReplace(req, "파일이 완전삭제되었습니다.", "../delArticle/genList");
	}
	
	// 복구
	@RequestMapping("/adm/delArticle/restore")
	public String doRestore(@RequestParam Map<String, Object> param, HttpServletRequest req) {

		int id = Util.getAsInt(param.get("id"), 0);
		
		if (id == 0) {
			return msgAndBack(req, "id를 입력해주세요.");
		}
		Article article = articleService.getArticle(id);

		if (article == null) {
			return msgAndBack(req, "해당 게시물은 존재하지 않습니다.");
		}
		
		articleService.doRestore(id);
				
		return msgAndReplace(req, String.format("%d번 게시물이 복구되었습니다.", id),
				"../delArticle/list");
	}
}