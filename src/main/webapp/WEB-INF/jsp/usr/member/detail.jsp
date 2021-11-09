<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.kh.board.util.Util"%>

<%@ include file="../part/mainLayoutHead.jspf"%>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<input type="hidden" name="genFileIdsStr" />
		<input type="hidden" name="id" value="${member.id}" />
		<div class="form-row flex flex-col lg:flex-row">
			<div class="lg:flex lg:items-center lg:w-28">
				<span>로그인아이디</span>
			</div>
			<div class="lg:flex-grow">${member.loginId}</div>
		</div>
		<div class="form-row flex flex-col lg:flex-row">
			<div class="lg:flex lg:items-center lg:w-28">
				<span>이름</span>
			</div>
			<div class="lg:flex-grow">${member.name}</div>
		</div>
		<div class="form-row flex flex-col lg:flex-row">
			<div class="lg:flex lg:items-center lg:w-28">
				<span>별명</span>
			</div>
			<div class="lg:flex-grow">${member.nickname}</div>
		</div>
		<div class="form-row flex flex-col lg:flex-row">
			<div class="lg:flex lg:items-center lg:w-28">
				<span>이메일</span>
			</div>
			<div class="lg:flex-grow">${member.email}</div>
		</div>
		<div class="form-row flex flex-col lg:flex-row">
			<div class="lg:flex lg:items-center lg:w-28">
				<span>전화번호</span>
			</div>
			<div class="lg:flex-grow">${member.cellphoneNo}</div>
		</div>
		<div class="form-row flex flex-col lg:flex-row">
			<div class="lg:flex-grow">
				<div class="btns">
					<a href="modify?id=${member.id}"
						class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded">
						수정</a>
				</div>
			</div>
		</div>
	</div>
</section>

<%@ include file="../part/mainLayoutFoot.jspf"%>