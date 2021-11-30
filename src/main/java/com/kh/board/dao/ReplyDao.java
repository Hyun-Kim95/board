package com.kh.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.board.dto.Reply;

@Mapper
public interface ReplyDao {
	List<Reply> getForPrintReplies(@Param("articleId") int relId);

	Reply getReply(@Param("id") int id);

	void deleteReply(@Param("id") int id);

	void modifyReply(@Param("id") int id, @Param("body") String body);
	
	void addReply(@Param("articleId") int articleId, @Param("memberId") int memberId, @Param("body") String body);

	void deleteReplies(@Param("articleId")int articleId);
}