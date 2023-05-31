<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script src="/js/account.js" type="text/javascript"></script>
<script src="/js/article.js" type="text/javascript"></script>
<nav class="navbar navbar-expand-lg bg-body-tertiary shadow fixed-top">
	<div class="container">
		<a class="navbar-brand link-primary fw-bold fs-3" href="/">
			<img id="header-logo" src="/assets/img/common/logo.png" alt=""
			width="100px" />
		</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse fw-bold"
			id="navbarSupportedContent">
			<!-- 로그인 전 -->
			<c:if test="${empty loginInfo}">
				<ul class="navbar-nav mb-2 mb-lg-0 ms-auto logged-out">
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="#" data-bs-toggle="modal"
						data-bs-target="#registerModal">회원가입</a></li>
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="#" data-bs-toggle="modal"
						data-bs-target="#loginModal">로그인</a></li>
				</ul>
			</c:if>
			<!-- 로그인 후 -->
			<c:if test="${not empty loginInfo}">
				<ul class="navbar-nav mb-2 mb-lg-0 ms-auto logged-in">
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="/search">지역별여행지</a>
					</li>
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="/schedule">나의여행계획</a>
					</li>
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="/hotplace">핫플자랑하기</a></li>
					<li class="nav-item"><a class="nav-link active"
						aria-current="page"
						onclick="searchArticle()"
						<%--href="${root}/article?action=tripinfo&order=none&keyword="  --%>>여행정보공유</a>
					</li>
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="/mypage">마이페이지</a></li>
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" onclick="logout()">로그아웃</a></li>
				</ul>
			</c:if>
		</div>
	</div>
</nav>
<!-- 상단 header nav end -->

<!-- 상단 header 고정을 위한 공백 start -->
<div style="height: 90px"></div>
<!-- 상단 header 고정을 위한 공백 end -->

<!-- 로그인 modal start -->
<div class="modal fade" id="loginModal" data-bs-backdrop="static"
	data-bs-keyboard="false" tabindex="-1"
	aria-labelledby="staticBackdropLabel" aria-hidden="true">
	<c:if test="${cookie.cookie_id.value ne null}">
		<c:set var="idck" value=" checked" />
		<c:set var="saveid" value="${cookie.cookie_id.value}" />
	</c:if>
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">
					<i class="bi bi-door-open text-info"> 로그인</i>
				</h4>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>

			<div class="modal-body">
				<form action="/user/login" id="login-form" method="POST">
					<div class="mb-3">
						<i class="bi bi-database" style="color: rgb(121, 2, 119)"></i> <label
							for="question" class="form-label">이메일:</label> <input type="text"
							class="form-control" id="login-id" placeholder="이메일" name="email"
							value="${saveid}" />
					</div>
					<div class="mb-3">
						<i class="bi bi-database-lock" style="color: rgb(121, 2, 119)"></i>
						<label for="question" class="form-label">비밀번호:</label> <input
							type="password" class="form-control" id="login-password"
							placeholder="비밀번호" name="password" />
					</div>
					<div class="form-check">
						<label class="form-check-label" for="id-checkbox"> <input
							class="form-check-input" type="checkbox" value="save"
							id="id-checkbox" name="saveid" ${idck} /> 아이디 저장
						</label>
					</div>
				</form>
			</div>

			<div class="modal-footer">
				<button type="submit" id="btn-login"
					class="btn btn-outline-primary btn-sm" onclick="login()">로그인</button>
				<button type="button" id="btn-find-id"
					class="btn btn-outline-success btn-sm" onclick="findId()">
					아이디 찾기</button>
				<button type="button" id="btn-find-password"
					class="btn btn-outline-danger btn-sm" onclick="findPassword()">
					비밀번호 찾기</button>
			</div>
		</div>
	</div>
</div>
<!-- 로그인 modal end -->

<!-- 회원가입 modal start -->
<div class="modal fade" id="registerModal" data-bs-backdrop="static"
	data-bs-keyboard="false" tabindex="-1"
	aria-labelledby="staticBackdropLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">
					<i class="bi bi-chat-left-dots-fill text-info"> 회원가입</i>
				</h4>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>

			<div class="modal-body">
				<form id="join-form" action="" method="POST">
					<input type="hidden" name="action" value="join" />
					<div class="mb-3">
						<i class="bi bi-database-lock" style="color: rgb(121, 2, 119)"></i>
						<label for="question" class="form-label">이메일 :</label>
						<div class="input-group mb-3">
							<input type="text" class="form-control" id="register-email"
								placeholder="이메일아이디" name="emailid" /> <span
								class="input-group-text">@</span> <select id="emali-domain"
								name="emaildomain" class="form-select">
								<option value="0" selected>선택</option>
								<option value="gmail.com">gmail.com</option>
								<option value="naver.com">naver.com</option>
								<option value="kakao.com">kakao.com</option>
								<option value="ssafy.com">ssafy.com</option>
							</select>
						</div>
					</div>
					<div class="mb-3">
						<i class="bi bi-database-lock" style="color: rgb(121, 2, 119)"></i>
						<label for="question" class="form-label">비밀번호 :</label> <input
							type="password" class="form-control" id="register-password"
							placeholder="비밀번호..." name="password" />
					</div>
					<div class="mb-3">
						<i class="bi bi-database-lock" style="color: rgb(121, 2, 119)"></i>
						<label for="question" class="form-label">비밀번호확인 :</label> <input
							type="password" class="form-control"
							id="register-password-confirm" placeholder="비밀번호확인..."
							name="password-confirm" />
					</div>
					<div class="mb-3">
						<i class="bi bi-database-lock" style="color: rgb(121, 2, 119)"></i>
						<label for="question" class="form-label">이름 :</label> <input
							type="text" class="form-control" id="register-name"
							placeholder="이름..." name="name" />
					</div>
					<div class="mb-3">
						<i class="bi bi-database" style="color: rgb(121, 2, 119)"></i> <label
							for="question" class="form-label">별명 :</label> <input type="text"
							class="form-control" id="register-id" placeholder="별명..."
							name="id" />
					</div>
				</form>
			</div>

			<div class="modal-footer">
				<button type="button" id="btn-register"
					class="btn btn-outline-primary btn-sm" onclick="register()">
					회원가입</button>
				<button type="button" class="btn btn-outline-success btn-sm"
					onclick="reset()">초기화</button>
				<button type="button" class="btn btn-outline-danger btn-sm"
					data-bs-dismiss="modal">닫기</button>
			</div>
		</div>
	</div>
</div>
<!-- 회원가입 modal end -->
<c:if test="${not empty msg}">
	<script>
		alert("${msg}");
		location.href = "/";
	</script>
</c:if>
