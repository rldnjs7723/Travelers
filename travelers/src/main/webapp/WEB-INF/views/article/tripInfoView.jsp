<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${root}/assets/styles/main.css" rel="stylesheet" />
<!-- <script src="/js/main.js" type="text/javascript"></script>
        <script src="/js/account.js" type="text/javascript"></script> -->
</head>
<body>
	<%@ include file="/WEB-INF/views/include/nav.jsp"%>

	<!-- 중앙 content start-->
	<section id="board-share-list" class="pt-3">
		<div class="container" data-aos="fade-up">
			<div class="section-header text-center">
				<h2>여행정보공유</h2>
				<div class="contour"></div>
			</div>

			<main>
			<div class="py-5 mt-5">
				<div class="py-3 my-3"></div>
				<small class="text-muted">여행 정보 공유</small>
				<h3 class="my-3">${article.subject}</h3>
				<i class="bi bi-emoji-smile"></i> <small class="text-muted">작성자: ${article.userId}</small> <br />
				<i class="bi bi-calendar"></i> <small class="text-muted">${article.registerTime}</small>
			</div>
			<div style="text-align: end">
				<i class="bi bi-chat-left-dots"></i> <small class="text-muted">1</small> &nbsp;
				<i class="bi bi-eye"></i> <small class="text-muted">${article.hit}</small>
			</div>

			<hr class="my-4" />

			<div class="row g-5">
				<div class="col-md-12 col-lg-12">
					<div class="my-3" style="text-align: center">
						<img src="./assets/img/board_img.jpg" style="width: 50em" />
					</div>
					<p>
						${article.content}
					</p>

					<hr class="my-4" />

					<small class="text-muted"> 댓글</small> <br /><br />
					<div class="my-3">
						<i class="bi bi-person-circle"></i> 안녕하세요~ \(^0^)/
					</div>
					<div class="my-3">
						<i class="bi bi-person-circle"></i> 여행 가고싶어요~~
					</div>

					<div class="row my-5">
						<small class="text-muted"> 댓글 작성</small>
						<textarea class="form-control mt-2 mx-2"
							id="exampleFormControlTextarea1" rows="3"></textarea>
						<div class="mt-2 pe-0" style="text-align: end">
							<a class="btn btn-outline-secondary w-5" type="button">등록</a>
						</div>
					</div>

					<div style="text-align: center">
						<a href="${root}/board?action=delete&articleno=${article.articleNo}" type="button" class="w-5 btn btn-danger mt-5">삭제</a>
						<a href="${root}/board?action=tripinfo&order=none&keyword=" class="w-5 btn btn-primary mt-5" type="button">목록으로</a>
						<a href="${root}/board?action=mvmodify&articleno=${article.articleNo}" class="w-5 btn btn-outline-primary mt-5" type="button">수정하기</a>
					</div>
				</div>
			</div>
			</main>
		</div>
	</section>
	<!-- 중앙 content end -->

	<%@ include file="/WEB-INF/views/include/footer.jsp"%>
</body>
</html>
