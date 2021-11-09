package com.kh.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.board.dto.Board;

@Mapper
public interface BoardDao {
	List<Board> getBoards();

	void addBoard(Map<String, Object> param);

	Board getBoard(@Param("id")int id);

	void modifyBoard(Map<String, Object> param);

	void deleteBoard(@Param("id")int id);
}
