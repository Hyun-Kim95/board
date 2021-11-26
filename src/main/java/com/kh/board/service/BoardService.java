package com.kh.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.board.dao.BoardDao;
import com.kh.board.dto.Board;
import com.kh.board.dto.ResultData;
import com.kh.board.util.Util;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;
	// 게시판 추가
	public ResultData addBoard(Map<String, Object> param) {
		boardDao.addBoard(param);

		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", "성공하였습니다.", "id", id);
	}

	public ResultData deleteBoard(int id) {
		boardDao.deleteBoard(id);

		return new ResultData("S-1", "삭제하였습니다.", "id", id);
	}

	public void modifyBoard(Map<String, Object> param) {
		boardDao.modifyBoard(param);
	}

	public List<Board> getBoards() {
		
		List<Board> boards = boardDao.getBoards();
				
		return boards;
	}

	public Board getBoard(int id) {
		return boardDao.getBoard(id);
	}
}
