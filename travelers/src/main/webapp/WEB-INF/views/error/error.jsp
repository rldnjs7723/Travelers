<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/include/header.jsp" %>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/nav.jsp" %>

	<%-- 페이지만의 내용 --%>
	<div class="container p-4">
		오류가 발생했습니다.
		<div>
			<a href="${root}">메인 화면으로 이동</a>
		</div>
	</div>
	<%-- --%>
<%@ include file="/WEB-INF/views/include/footer.jsp" %>