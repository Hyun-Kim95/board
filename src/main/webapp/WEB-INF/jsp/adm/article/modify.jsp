<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.kh.board.util.Util" %>

<%@ include file="../part/mainLayoutHead.jspf"%>

<c:set var="fileInputMaxCount" value="10" />
<script>
ArticleModify__fileInputMaxCount = parseInt("${fileInputMaxCount}");
const articleId = parseInt("${article.id}");
</script>

<script>
ArticleModify__submited = false;
function ArticleModify__checkAndSubmit(form) {
	if ( ArticleModify__submited ) {
		alert('처리중입니다.');
		return;
	}
	
	form.title.value = form.title.value.trim();

	if ( form.title.value.length == 0 ) {
		alert('제목을 입력해주세요.');
		form.title.focus();

		return false;
	}

	form.body.value = form.body.value.trim();
	
	if ( form.body.value.length == 0 ) {
		alert('내용을 입력해주세요.');
		form.body.focus();

		return false;
	}

	var maxSizeMb = 50;
	var maxSize = maxSizeMb * 1024 * 1024;
	
	for (let inputNo = 1; inputNo <= ArticleModify__fileInputMaxCount; inputNo++ ) {
		const input = form["file__" + articleId + "__" + inputNo];
		
		if (input.value) {
			if (input.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
				input.focus();
				
				return;
			}
		}
	}
	
	const startSubmitForm = function(data) {
		if (data && data.body && data.body.genFileIdsStr) {
			form.genFileIdsStr.value = data.body.genFileIdsStr;
		}
		
		for ( let inputNo = 1; inputNo <= ArticleModify__fileInputMaxCount; inputNo++ ) {
			const input = form["file__" + articleId + "__" + inputNo];
			input.value = '';
		}
		
		for ( let inputNo = 1; inputNo <= ArticleModify__fileInputMaxCount; inputNo++ ) {
			const input = form["deleteFile__" + articleId + "__" + inputNo];
			if ( input ) {
				input.checked = false;
			}
		}
		form.submit();
	};

	const startUploadFiles = function(onSuccess) {
		var needToUpload = false;

		for ( let inputNo = 1; inputNo <= ArticleModify__fileInputMaxCount; inputNo++ ) {
			const input = form["file__" + articleId + "__" + inputNo];

			if ( input.value.length > 0 ) {
				needToUpload = true;
				break;
			}
		}
		
		if ( needToUpload == false ) {
			for ( let inputNo = 1; inputNo <= ArticleModify__fileInputMaxCount; inputNo++ ) {
				const input = form["deleteFile__" + articleId + "__" + inputNo];
				if ( input && input.checked ) {
					needToUpload = true;
					break;
				}
			}
		}
		
		if (needToUpload == false) {
			onSuccess();
			return;
		}
		
		var fileUploadFormData = new FormData(form);
		
		$.ajax({
			url : '/common/genFile/doUpload',
			data : fileUploadFormData,
			processData : false,
			contentType : false,
			dataType : "json",
			type : 'POST',
			success : onSuccess
		});
	}

	ArticleModify__submited = true;

	startUploadFiles(startSubmitForm);
}
</script>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<form onsubmit="ArticleModify__checkAndSubmit(this); return false;" action="doModify" method="POST" enctype="multipart/form-data">
			<input type="hidden" name="genFileIdsStr" value="" />
			<input type="hidden" name="id" value="${article.id}" />
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>제목</span>
				</div>
				<div class="lg:flex-grow">
					<input value="${article.title}" type="text" name="title" autofocus="autofocus"
						class="form-row-input w-full rounded-sm" placeholder="제목을 입력해주세요." />
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>내용</span>
				</div>
				<div class="lg:flex-grow">
					<textarea name="body" class="form-row-input w-full rounded-sm" placeholder="내용을 입력해주세요.">${article.body}</textarea>
				</div>
			</div>
			<c:forEach begin="1" end="${fileInputMaxCount}" var="inputNo">
				<c:set var="fileNo" value="${String.valueOf(inputNo)}" />
                <c:set var="file" value="${article.extra.file[fileNo]}" />
				<div class="form-row flex flex-col lg:flex-row">
					<div class="lg:flex lg:items-center lg:w-28">
						<span>첨부파일 ${inputNo}</span>
					</div>
					<div class="lg:flex-grow input-file-wrap">
						<input type="file" name="file__${article.id}__${inputNo}"
							class="form-row-input w-full rounded-sm"/>
						<c:if test="${file != null}">
							<div>
								<a class="text-blue-500 hover:underline" href="${file.forPrintUrl}" target="_blank">${file.originFileName}</a> ( ${Util.numberFormat(file.fileSize)} Byte )
							</div>
							<div>
								<label>
									<input type="checkbox" name="deleteFile__${article.id}__${fileNo}" value="Y" />
									<span>삭제</span>
                            	</label>
							</div>
							<c:if test="${file.fileExtTypeCode == 'img'}">
	                            <div class="img-box img-box-auto">
	                            	<a class="inline-block" href="${file.forPrintUrl}" target="_blank" title="자세히 보기">
	                            		<img class="max-w-sm" src="${file.forPrintUrl}">
	                            	</a>
	                            </div>
                            </c:if>
						</c:if>
					</div>
				</div>
			</c:forEach>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex-grow">
					<div class="btns">
						<input type="submit" class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded" value="수정">
						<input onclick="history.back();" type="button" class="btn-info bg-red-500 hover:bg-red-dark text-white font-bold py-2 px-4 rounded" value="취소">
					</div>
				</div>
			</div>
		</form>
	</div>
</section>

<%@ include file="../part/mainLayoutFoot.jspf"%>