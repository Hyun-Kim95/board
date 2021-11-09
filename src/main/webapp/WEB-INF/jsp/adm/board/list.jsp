<%@ page import="com.kh.board.util.Util"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../part/mainLayoutHead.jspf"%>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<div class="flex items-center">
			<a href="add?id=" class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded">게시판 생성</a>
		</div>

		<div>
			<c:forEach items="${boards}" var="board">
				<div class="bg-gray-100 m-1 p-2 shadow-md">
					<c:set var="goList" value="../article/list?boardId=${board.id}" />
					<div class="flex items-center mt-2">
						<a href="${goList}" class="font-bold">Code. ${board.code}</a>
						<script>param.id = parseInt("${board.id}");</script>
					</div>
					<div class="mt-2">
						<a href="${goList}" class="text-2xl text-gray-700 font-bold hover:underline">${board.name}</a>
					</div>
					<div class="flex items-center mt-4">
						<a href="modify?id=${board.id}" class="btn-primary bg-green-500 hover:bg-blue-dark text-white font-bold py-2 px-2 m-1 rounded">수정</a>
						<a onclick="if ( !confirm('삭제하시겠습니까?') ) return false;" href="doDelete?id=${board.id}" class="btn-primary bg-red-500 hover:bg-blue-dark text-white font-bold py-2 px-2 rounded">삭제</a>
						<div class="flex-grow"></div>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</section>

<%@ include file="../part/mainLayoutFoot.jspf"%>