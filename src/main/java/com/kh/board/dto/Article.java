package com.kh.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article extends EntityDto {
	private int id;
	private String regDate;
	private String updateDate;
	private String delDate;
	private int delStatus;
	private int boardId;
	private int memberId;
	private String title;
	private String body;

	private String extra__writer;
	private String extra__boardName;
	
}
