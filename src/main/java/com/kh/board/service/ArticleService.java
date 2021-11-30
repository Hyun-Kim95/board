package com.kh.board.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.board.dao.ArticleDao;
import com.kh.board.dao.ReplyDao;
import com.kh.board.dto.Article;
import com.kh.board.dto.Board;
import com.kh.board.dto.GenFile;
import com.kh.board.dto.Member;
import com.kh.board.dto.ResultData;
import com.kh.board.util.Util;

@Service
public class ArticleService {
	@Autowired
	private GenFileService genFileService;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ReplyDao replyDao;

	public Article getArticle(int id) {
		return articleDao.getArticle(id);
	}

	public int addArticle(Map<String, Object> param) {
		articleDao.addArticle(param);

		int id = Util.getAsInt(param.get("id"), 0);
		// 첨부파일의 relId 변경(0 -> article의 Id)
		genFileService.changeInputFileRelIds(param, id);

		return id;
	}

	public void deleteArticle(int id) {	// 삭제가 아닌 delId와 delStatus를 변경
		replyDao.deleteReplies(id);
		
		articleDao.deleteChangeArticle(id);

		genFileService.changeDeleteGenFilesByRelId(id);
	}

	public int modifyArticle(Map<String, Object> param) {
		articleDao.modifyArticle(param);
		
		int id = Util.getAsInt(param.get("id"), 0);
		
		return id;
	}

	public List<Article> getArticles(String searchKeywordType, String searchKeyword) {
		return articleDao.getArticles(searchKeywordType, searchKeyword);
	}
	// 로그인 한 ID가 수정 가능한 Id 인지 확인
	public ResultData getActorCanModifyRd(Article article, Member actor) {
		if (article.getMemberId() == actor.getId()) {
			return new ResultData("S-1", "가능합니다.");
		}

		if (memberService.isAdmin(actor)) {
			return new ResultData("S-2", "가능합니다.");
		}

		return new ResultData("F-1", "권한이 없습니다.");
	}

	public ResultData getActorCanDeleteRd(Article article, Member actor) {
		return getActorCanModifyRd(article, actor);
	}

	public Article getForPrintArticle(int id) {
		return articleDao.getForPrintArticle(id);
	}

	public List<Article> getForPrintArticles(int boardId, String searchKeywordType, String searchKeyword, int page,
			int itemsInAPage) {
		
		int limitStart = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;
		
		List<Article> articles = articleDao.getForPrintArticles(boardId, searchKeywordType, searchKeyword, limitStart, limitTake);
		List<Integer> articleIds = articles.stream().map(article -> article.getId()).collect(Collectors.toList());
		Map<Integer, Map<String, GenFile>> filesMap = genFileService.getFilesMapKeyRelIdAndFileNo(articleIds);
		
		for (Article article : articles) {
			Map<String, GenFile> mapByFileNo = filesMap.get(article.getId());

			if (mapByFileNo != null) {
				article.getExtraNotNull().put("file", mapByFileNo);
			}
		}
		
		return articles;
	}

	public Board getBoard(int id) {
		return articleDao.getBoard(id);
	}
	// 게시물의 전체 갯수 리턴
	public int getArticlesTotalCount(int boardId, String searchKeywordType, String searchKeyword) {
		return articleDao.getArticlesTotalCount(boardId, searchKeywordType, searchKeyword);
	}

	public List<Article> getArticlesBoardId(int id) {
		return articleDao.getArticlesBoardId(id);
	}
	// 완전 삭제
	public void deleteCompletelyArticle(int id) {
		articleDao.deleteArticle(id);

		genFileService.deleteGenFiles(id);
	}
	// 삭제된 파일들의 전체 갯수 리턴
	public int getArticlesTotalCountByDel(int boardId, String searchKeywordType, String searchKeyword) {
		return articleDao.getArticlesTotalCountByDel(boardId, searchKeywordType, searchKeyword);
	}
	// 삭제된 파일 복구
	public void doRestore(int id) {
		genFileService.restoreGenFilesByRelId(id);
		articleDao.restoreArticle(id);
	}
}
