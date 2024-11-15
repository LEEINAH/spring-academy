<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "com.myaws.myapp.domain.*" %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 메세지 출력 -->
<c:set var="msg" value="${requestScope.msg}" />
<c:if test="${!empty msg}">
    <script>alert('${msg}');</script>
</c:if> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글수정</title>
<link href="${pageContext.request.contextPath}/resources/css/style2.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-latest.min.js"></script> 
<!-- jquery CDN 주소 -->
<script> 

function checkImageType(fileName) {
	
	var pattern = /jpg$|gif$|png$|jpeg$/i;     // 자바 스크립트 정규 표현식
	return fileName.match(pattern);
}

function getOriginalFileName(fileName) { // 원본 파일 이름 추출
	
	var idx = fileName.lastIndexOf("_")+1;	
	return fileName.substr(idx);
}

function getImageLink(fileName) {
	
	var front = fileName.substr(0, 12);	
	var end = fileName.substr(14);
	return front+end;
}

function download() {
	
	var downloadImage = getImageLink("${bv.filename}");
	var downLink = "${pageContext.request.contextPath}/board/displayFile.aws?fileName="+downloadImage+"&down=1";
	return downLink;
}

$(document).ready(function() {
	
	$("#dUrl").html(getOriginalFileName("${bv.filename}"))
	
	$("#dUrl").click(function () {
		$("#dUrl").attr("href",download());
		return;
	});
});

function check() {
	  
	  // 유효성 검사하기
	  let fm = document.frm; // 문서 객체 안에 폼 객체
	  
	  if (fm.subject.value == "") {
		  alert("제목을 입력해주세요");
		  fm.subject.focus();
		  return;
	  } else if (fm.contents.value == "") {
		  alert("내용을 입력해주세요");
		  fm.contents.focus();
		  return;
	  } else if (fm.writer.value == "") {
		  alert("작성자를 입력해주세요");
		  fm.writer.focus();
		  return;
	  } else if (fm.password.value == "") {
		  alert("비밀번호를 입력해주세요");
		  fm.password.focus();
		  return;
	  }
	  
	  let ans = confirm("저장하시겠습니까?"); // 함수의 값을 참과 거짓 true false로 나눈다
	  
	  if (ans == true) {
		  fm.action = "${pageContext.request.contextPath}/board/boardModifyAction.aws";
		  fm.method="post";
		  fm.enctype="multipart/form-data";
		  fm.submit();
	  }	  
	  
	  return;
}

</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글수정</h2>
</header>

<form name="frm">
<input type="hidden" name="bidx" value="${bv.bidx}">
<input type="hidden" name="Midx" value="${bv.midx}">
	<table class="writeTable">
		<tr>
			<th>제목</th>
			<td><input type="text" name="subject" value="${bv.subject}"></td>
		</tr>
		<tr>
			<th>내용</th>
			<td><textarea name="contents" rows="6">${bv.contents}</textarea></td>
		</tr>
		<tr>
			<th>작성자</th>
			<td><input type="text" name="writer" value="${bv.writer}"></td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td><input type="password" name="password"></td>
		</tr>
		<tr>
			<th>첨부파일</th>
			<td>
			<input type="file" name="attachfile"></td>
		</tr>
	</table>
	
	<div class="btnBox">
		<button type="button" class="btn" onclick="check();">저장</button>
		<a class="btn aBtn" href="${pageContext.request.contextPath}/board/boardContents.aws?bidx=${bv.bidx}" onclick="history.back();">취소</a>
	</div>	
</form>

</body>
</html>