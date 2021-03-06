package com.kh.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.board.dto.Article;
import com.kh.board.dto.Board;

@Mapper
public interface ArticleDao {
	Article getArticle(@Param("id") int id);

	void addArticle(Map<String, Object> param);
	
	void deleteChangeArticle(@Param("id") int id);

	void deleteArticle(@Param("id") int id);

	void modifyArticle(Map<String, Object> param);

	List<Article> getArticles(@Param("searchKeywordType") String searchKeywordType,
			@Param("searchKeyword") String searchKeyword);

	Article getForPrintArticle(@Param("id") int id);

	List<Article> getForPrintArticles(@Param("boardId") int boardId,
			@Param("searchKeywordType") String searchKeywordType, @Param("searchKeyword") String searchKeyword,
			@Param("limitStart") int limitStart, @Param("limitTake") int limitTake);

	Board getBoard(@Param("id") int id);

	void addReply(Map<String, Object> param);

	int getArticlesTotalCount(@Param("boardId") int boardId, @Param("searchKeywordType") String searchKeywordType,
			@Param("searchKeyword") String searchKeyword);

	List<Article> getArticlesBoardId(@Param("id") int id);

	int getArticlesTotalCountByDel(@Param("boardId") int boardId, @Param("searchKeywordType") String searchKeywordType,
			@Param("searchKeyword") String searchKeyword);

	void restoreArticle(@Param("id")int id);

	int getArticlesTotalCountByDelGenFile();

	List<Article> getArticlesByDel();
}
