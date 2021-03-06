package com.kh.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.board.dto.Member;

@Mapper
public interface MemberDao {
	void join(Map<String, Object> param);

	Member getMember(@Param("id") int id);
	
	Member getMemberByLoginId(@Param("loginId") String loginId);

	void modifyMember(Map<String, Object> param);

	Member getMemberByAuthKey(@Param("authKey") String authKey);

	List<Member> getForPrintMembers(Map<String, Object> param);

	Member getForPrintMember(@Param("id") int id);

	void deleteMember(@Param("id")int id);

	String getNickname(@Param("id")int id);
}
