package com.kh.board.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kh.board.dao.ArticleDao;
import com.kh.board.dao.GenFileDao;
import com.kh.board.dto.Article;
import com.kh.board.dto.GenFile;

// 일정 기간이 지난 삭제 파일을 삭제하는 함수 구현
@Component
public class Scheduler {
	@Autowired
	private GenFileDao genFileDao;
	@Autowired
	private ArticleDao articleDao;
	
	// 매일 오후 12시에 실행
	@Scheduled(cron = "0 0 12 * * ?")
	public void cronJobSch() throws ParseException {
		// 삭제된 게시물들 확인
		List<Article> articles = articleDao.getArticlesByDel();
		for (Article article : articles) {
			// 7일이 경과하면 완전 삭제 
			if(CalDate(article.getDelDate()) > 7) {
				articleDao.deleteArticle(article.getId());
			}
		}
		// 게시물 삭제 후에 남아있는 첨부파일들 확인
		List<GenFile> genFiles = genFileDao.getGenFilesByDel();
		for (GenFile genFile : genFiles) {
			// 7일 경과하면 완전 삭제
			if(CalDate(genFile.getDelDate()) > 7) {
				genFileDao.deleteFile(genFile.getId());
			}
		}
	}
	
	// 경과시간 확인 함수
	public long CalDate(String DelDate) throws ParseException {
		
		Calendar getToday = Calendar.getInstance();
		getToday.setTime(new Date()); //금일 날짜
		
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(DelDate);
		Calendar cmpDate = Calendar.getInstance();
		cmpDate.setTime(date); //삭제 일자
		
		long diffSec = (getToday.getTimeInMillis() - cmpDate.getTimeInMillis()) / 1000;	// 초 차이
		long diffDays = diffSec / (24*60*60); //일 차이
		
		// 일 차이 리턴
		return diffDays;
	}
}
