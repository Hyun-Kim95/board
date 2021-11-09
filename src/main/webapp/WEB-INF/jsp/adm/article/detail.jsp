<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.kh.board.util.Util"%>

<%@ include file="../part/mainLayoutHead.jspf"%>

<c:set var="fileInputMaxCount" value="10" />

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<div class="w-full">
			<div>
				<a href="../article/list?boardId=${board.id}">${board.name}</a>
			</div>
			<div class="flex flex-row mt-2 py-3">
				<div class="flex flex-col mb-2 ml-4 mt-1">
					<div class="text-gray-600 font-semibold">작성자 : ${article.extra__writer}</div>
					<div class="flex w-full mt-1">
						<div class="text-gray-400 font-thin text-xs">${article.regDate}</div>
					</div>
				</div>
			</div>
			<div class="border-b border-gray-100"></div>
			<div class="text-gray-400 font-medium text-sm mb-7 mt-6">
				<c:forEach begin="1" end="${fileInputMaxCount}" var="inputNo">
					<c:set var="fileNo" value="${String.valueOf(inputNo)}" />
					<c:set var="file" value="${article.extra.file__common__attachment[fileNo]}" />
					${file.mediaHtml}
				</c:forEach>
			</div>
			<div class="text-gray-600 font-semibold text-lg mb-2">${article.title}</div>
			<div class="text-gray-500 font-thin text-sm mb-6">${article.body}</div>
		</div>
	</div>
</section>

<%@ include file="../part/mainLayoutFoot.jspf"%>