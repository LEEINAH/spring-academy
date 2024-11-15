<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "com.myaws.myapp.domain.*" %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
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

function commentDel(cidx) {
	
	let ans = confirm("삭제 하시겠습니까?");
	
	if (ans == true) {
		$.ajax({
			type : "get", // 전송 방식
			url : "${pageContext.request.contextPath}/comment/"+cidx+"/commentDeleteAction.aws",
			// 가상 경로
			dataType : "json", // json 타입은 문서에서 {"키 값" : "value 값", "키 값 2" : "value 값 2"}
			success : function	(result) { // 결과가 넘어와서 성공했을 때 받는 영역
				// alert("전송 성공 테스트");	
				// alert(result.value);
				$.boardCommentList();
			},
			error : function () { // 결과가 넘어와서 실패했을 때 받는 영역
				alert("전송 실패");
			}
		});
	}
	
	return;
}

//jquery로 만드는 함수
$.boardCommentList = function () { 
	
	let block = $("#block").val();
	
	$.ajax({
		type : "get", // 전송 방식
		url : "${pageContext.request.contextPath}/comment/${bv.bidx}/"+block+"/commentList.aws",
		// 가상 경로
		dataType : "json", // json 타입은 문서에서 {"키 값" : "value 값", "키 값 2" : "value 값 2"}
		success : function	(result) { // 결과가 넘어와서 성공했을 때 받는 영역
			// alert("전송 성공 테스트");
			var strTr = "";
			
			$(result.clist).each(function() {
				var btnn = "";
				// 현재 로그인 한 사람과 댓글 쓴 사람의 번호가 같을 때만 나타내준다
				if (this.midx == "${midx}") {
					if (this.delyn == "N") {
						btnn ="<button type='button' onclick='commentDel("+this.cidx+");'>삭제</button>";
					}
				}
				
				strTr = strTr + "<tr>"
				+"<td>"+this.cidx+"</td>"
				+"<td>"+this.cwriter+"</td>"
				+"<td class='content'>"+this.ccontents+"</td>"
				+"<td>"+this.writeday+"</td>"
				+"<td>"+btnn+"</td>"
				+"</tr>";
			});
			
			var str = "<table class='replyTable'>"
			+"<tr>"
			+"<th>번호</th>"
			+"<th>작성자</th>"
			+"<th>내용</th>"
			+"<th>날짜</th>"
			+"<th>DEL</th>"
			+"</tr>"+strTr+"</table>";	
			
			$("#commentListView").html(str);
			
			if (result.moreView == "N") {
				$("#morebtn").css("display", "none"); // 감춘다			
			} else {
				$("#morebtn").css("display", "block"); // 보여준다
			}
			
			$("#block").val(result.nextBlock);
		},
		error : function () { // 결과가 넘어와서 실패했을 때 받는 영역
			// alert("전송 실패");
		}
	});
}

$(document).ready(function() {
	
	$("#dUrl").html(getOriginalFileName("${bv.filename}"))
	
	$("#dUrl").click(function () {
		$("#dUrl").attr("href",download());
		return;
	});
	
	$("#btn").click(function() {
		// alert("추천 버튼 클릭");
		
		$.ajax({
			type : "get", // 전송 방식
			url : "${pageContext.request.contextPath}/board/boardRecom.aws?bidx=${bv.bidx}",
			// 가상 경로
			dataType : "json", // json 타입은 문서에서 {"키 값" : "value 값", "키 값 2" : "value 값 2"}
			success : function	(result) { // 결과가 넘어와서 성공했을 때 받는 영역
				// alert("전송 성공 테스트");	
			
				var str ="추천("+result.recom+")";
			
				$("#btn").val(str);
			},
			error : function () { // 결과가 넘어와서 실패했을 때 받는 영역
				alert("전송 실패");
			}
		});
	});
	
	$.boardCommentList();
	
	$("#cmtBtn").click(function() {
		
		let loginCheck = "${midx}";
		
		if (loginCheck == 0) {
			alert("로그인을 해주세요.");
			return;
		}
		
		// 유효성 검사
		let cwriter = $("#cwriter").val();
		let ccontents = $("#ccontents").val();
		
		if (cwriter == "") {
			alert("작성자를 입력해주세요.");
			$("#cwriter").focus();
			return;
		} else if (ccontents == "") {
			alert("내용을 입력해주세요.");
			$("#ccontents").focus();
			return;
		}
		
		$.ajax({
			type : "post", // 전송 방식
			url : "${pageContext.request.contextPath}/comment/commentWriteAction.aws",
			data : {"cwriter" : cwriter, 
					"ccontents" : ccontents, 
					"bidx" : "${bv.bidx}", 
					"midx" : "${midx}"},
			// 가상 경로
			dataType : "json", // json 타입은 문서에서 {"키 값" : "value 값", "키 값 2" : "value 값 2"}
			success : function	(result) { // 결과가 넘어와서 성공했을 때 받는 영역
				// alert("전송 성공 테스트");	
				// var str ="("+result.value+")";
				// alert(str);
				if (result.value == 1) {
					$("#ccontents").val("");
					$("#block").val(1);
				}
				$.boardCommentList();
			},
			error : function () { // 결과가 넘어와서 실패했을 때 받는 영역
				alert("전송 실패");
			}
		});
	});
	
	$("#moreBtn").click(function() {
		$.boardCommentList();
	});
});

</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글내용</h2>
</header>

<article class="detailContents">
	<h2 class="contentTitle">${bv.subject} (조회수:${bv.viewcnt})</h2>
	<input type="button" id="btn" value="추천(${bv.recom})">
	<p class="write">${bv.writer} (${bv.writeday})</p>

	<hr>
	
	<div class="content">
		${bv.contents}
	</div>

	<c:if test="${!empty bv.filename}">
		<img src="${pageContext.request.contextPath}/board//displayFile.aws?fileName=${bv.filename}">
		<p>
		<a id="dUrl" href="#" class="fileDown">
		첨부파일 다운로드</a></p>
	</c:if>
	
</article>
	
<div class="btnBox">
	<a class="btn aBtn" href="${pageContext.request.contextPath}/board/boardModify.aws?bidx=${bv.bidx}">수정</a>
	<a class="btn aBtn" href="${pageContext.request.contextPath}/board/boardDelete.aws?bidx=${bv.bidx}">삭제</a>
	<a class="btn aBtn" href="${pageContext.request.contextPath}/board/boardReply.aws?bidx=${bv.bidx}">답변</a>
	<a class="btn aBtn" href="${pageContext.request.contextPath}/board/boardList.aws">목록</a>
</div>

<article class="commentContents">
	<form name="frm">
		<p class="commentWriter" style="width:100px;">
		<input type="text" id="cwriter" name="cwriter" value="${memberName}" readonly="readonly" style="width:100px;border:0px; text-align:center;">
		</p>	
		<input type="text" id="ccontents"  name="ccontents">
		<button type="button" id="cmtBtn" class="replyBtn" style="background-color:skyblue; color:white;">댓글쓰기</button>
	</form>	
	<div id="commentListView"></div>
	
	<input type="hidden" id="block" value="1">
	<div id="morebtn" style="text-align:center; line-height:50px;">
		<button type="button" id="moreBtn">더보기</button>
	</div>
</article>

</body>
</html>