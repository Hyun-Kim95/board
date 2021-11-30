package com.kh.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply extends EntityDto {
	private int id;
	private String regDate;
	private String updateDate;
	private int articleId;
	private int memberId;
	private String body;

	private String extra__writer;
}
