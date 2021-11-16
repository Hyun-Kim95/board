<%@ page import="com.kh.board.util.Util"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../part/mainLayoutHead.jspf"%>

<script>
	param.boardId = parseInt("${board.id}");
</script>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<div>
			<a href="list?boardId=1" class="px-2 py-1 bg-gray-600 text-gray-100 font-bold rounded hover:bg-gray-500">삭제된 게시물 모음</a>
		</div>
		<div class="flex items-center">
			<select class="py-2 select-board-id">
				<c:forEach items="${boards}" var="b">
					<option value="${b.id}">${b.name}</option>
				</c:forEach>
			</select>
			<script>
				$('.section-1 .select-board-id').val(param.boardId);

				$('.section-1 .select-board-id').change(function() {
					location.href = 'list?boardId=' + this.value;
				});
			</script>

		</div>

		<div>총 파일 수 : ${Util.numberFormat(totalItemsCount)}</div>

		<div>
			<c:forEach items="${genFiles}" var="genFile">
				<div class="bg-gray-100 m-1 p-2 shadow-md">
					<div class="items-center mt-10">
						<div class="font-bold">게시물 번호 : ${genFile.relId}</div>
						<div class="mt-1 font-light text-gray-600">삭제일시 : ${genFile.delDate}</div>
					</div>
					<div>
						<c:if test="${genFile.fileExtTypeCode == 'jpg' || genFile.fileExtTypeCode == 'img'}">
								${genFile.mediaHtml}
							</c:if>
							<c:if test="${genFile.fileExtTypeCode != null && genFile.fileExtTypeCode != 'jpg' && genFile.fileExtTypeCode != 'img'}">
								${genFile.originFileName}
						</c:if>
					</div>
					<div class="flex items-center mt-4">
						<a href="${genFile.downloadUrl}" target="_blank"
							class="text-blue-500 hover:underline">다운로드</a>
						<a onclick="if ( !confirm('완전삭제하시겠습니까?') ) return false;"
							href="deleteGenCompletely?id=${genFile.id}"
							class="ml-2 text-blue-500 hover:underline">완전삭제</a>
						<div class="flex-grow"></div>
					</div>
				</div>
			</c:forEach>
		</div>
		<!-- 페이징 시작 -->
		<nav class="flex justify-center rounded-md mt-3"
			aria-label="Pagination">
			<c:if test="${pageMenuStart != 1}">
				<a href="${Util.getNewUrl(requestUrl, 'page', 1)}"
					class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
					<span class="sr-only">Previous</span> <i
					class="fas fa-chevron-left"></i>
				</a>
			</c:if>

			<c:forEach var="i" begin="${pageMenuStart}" end="${pageMenuEnd}">
				<c:set var="aClassStr"
					value="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium" />
				<c:if test="${i == page}">
					<c:set var="aClassStr"
						value="${aClassStr} text-red-700 hover:bg-red-50" />
				</c:if>
				<c:if test="${i != page}">
					<c:set var="aClassStr"
						value="${aClassStr} text-gray-700 hover:bg-gray-50" />
				</c:if>
				<a href="${Util.getNewUrl(requestUrl, 'page', i)}"
					class="${aClassStr}">${i}</a>
			</c:forEach>

			<c:if test="${pageMenuEnd != totalPage}">
				<a href="${Util.getNewUrl(requestUrl, 'page', totalPage)}"
					class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">

					<span class="sr-only">Next</span> <i class="fas fa-chevron-right"></i>
				</a>
			</c:if>
		</nav>
		<!-- 페이징 끝 -->
	</div>
</section>

<%@ include file="../part/mainLayoutFoot.jspf"%>