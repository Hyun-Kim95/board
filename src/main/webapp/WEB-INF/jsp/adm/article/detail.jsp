<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.kh.board.util.Util"%>

<%@ include file="../part/mainLayoutHead.jspf"%>

<c:set var="fileInputMaxCount" value="10" />

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<div class="w-full">
			<div class="flex items-center mt-4">
				<a href="../article/list?boardId=${board.id}" class="px-2 py-1 bg-gray-600 text-gray-100 font-bold rounded hover:bg-gray-500">${board.name}</a>
				<div class="flex-grow"></div>
				<a href="modify?id=${article.id}"
					class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold mx-1 py-1 px-2 rounded">수정</a>
				<a onclick="if ( !confirm('삭제하시겠습니까?') ) return false;"
					href="doDelete?id=${article.id}"
					class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold py-1 px-2 rounded">삭제</a>
			</div>
			<div class="flex flex-row mt-2 py-3">
				<div class="flex flex-col mb-2 mt-1">
					<div class="text-gray-600 font-semibold">작성자 : ${article.extra__writer}</div>
					<div class="text-gray-400 font-thin text-xs">${article.regDate}</div>
				</div>
			</div>
			<div class="border-b border-gray-100"></div>
			<div class="text-gray-400 font-medium text-sm mb-7 mt-6">
				<c:forEach begin="1" end="${fileInputMaxCount}" var="inputNo">
					<c:set var="fileNo" value="${String.valueOf(inputNo)}" />
					<c:set var="file" value="${article.extra.file[fileNo]}" />
					<a href="${file.downloadUrl}" target="_blank" class="w-full text-blue-500 hover:underline">
						<c:if test="${file.fileExtTypeCode == 'jpg' || file.fileExtTypeCode == 'img'}">
							${file.mediaHtml}<br>
						</c:if>
						<c:if test="${file.fileExtTypeCode != null && file.fileExtTypeCode != 'jpg' && file.fileExtTypeCode != 'img'}">
							${file.originFileName}<br>
						</c:if>
					</a>
				</c:forEach>
			</div>
			<div class="text-gray-600 font-semibold text-lg mb-2">제목 : ${article.title}</div>
			<div class="text-gray-500 font-thin text-sm mb-6">${article.body}</div>
		</div>
		<!--  댓글  -->
		<script type="text/javascript">
		ReplyAdd__submited = false;
		function ReplyAdd__checkAndSubmit(form){
			if ( ReplyAdd__submited ) {
				alert('처리중입니다.');
				return;
			}
			
			form.body.value = form.body.value.trim();
			if ( form.body.value.length == 0 ) {
				alert('내용을 입력해주세요.');
				form.body.focus();
				
				return false;
			}
			
			ReplyAdd__submited = true;
			form.submit();
		}
		ReplyModify__submited = false;
		function ReplyModify__checkAndSubmit(form){
			if (ReplyModify__submited){
				alert('처리중입니다');
				return;
			}
			
			form.body_modify.value = form.body_modify.value.trim();
			if(form.body_modify.value.length == 0){
				alert('내용을 입력해주세요.');
				form.body_modify.focus();
				
				return false;
			}
			
			ReplyModify__submited = true;
			form.submit();
		}
		</script>
		<form onsubmit="ReplyAdd__checkAndSubmit(this); return false;" action="../reply/add" method="POST" class="flex">
	        <div class="lg:flex-grow">
	        	<input type="hidden" name="articleId" value="${article.id}">
				<input type="text" name="body" class="form-row-input w-full rounded-sm border border-gray-100" placeholder="댓글을 입력해주세요.">
			</div>
	        <div>
	       		<input type="submit" name="save" class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded" value="등록">
	        </div>
        </form>
       	<c:forEach items="${replies}" var="reply">
       		<div class="flex-grow px-1 border-b border-gray-100">
       			<div class="flex text-gray-400 text-light text-sm">
       				<span>${reply.extra__writer}</span>
       				<span class="mx-1">·</span>
                   	<span>${reply.updateDate}</span>
       			</div>
       			<div class="break-all">
       				<form onsubmit="ReplyModify__checkAndSubmit(this); return false;" action="../reply/doModify?id=${reply.id}" method="POST" class="flex">
	       				<c:if test="${loginedMemberId != reply.memberId}">
	       					<input type="text" value="${reply.body}" readonly>
	       				</c:if>
	       				<c:if test="${loginedMemberId == reply.memberId}">
	       					<input type="text" name="body_modify" value="${reply.body}">
		       				<input type="submit" value="수정"
								class="btn-primary bg-gray-500 hover:bg-blue-dark text-white font-bold mx-1 py-1 px-2 rounded">
	       				</c:if>
	       				<a onclick="if ( !confirm('삭제하시겠습니까?') ) return false;" href="../reply/doDelete?id=${reply.id}"
							class="btn-primary bg-gray-500 hover:bg-blue-dark text-white font-bold mx-1 py-1 px-2 rounded">삭제</a>
					</form>
       			</div>
       		</div>
       	</c:forEach>
    </div>
</section>

<%@ include file="../part/mainLayoutFoot.jspf"%>