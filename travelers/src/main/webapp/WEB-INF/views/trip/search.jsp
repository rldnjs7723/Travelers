<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<%@ include file="/WEB-INF/views/include/key.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/nav.jsp"%>

	<!-- 중앙 content start -->
	<div class="container">
		<div style="height: 70px"></div>
		<div class="row">
			<!-- 중앙 left content  start -->
			<div class="col-md-3">
				<!-- 관광지 리스트 Start -->
				<div class="row">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>관광지명</th>
							</tr>
						</thead>
						<tbody id="trip-list"></tbody>
					</table>
				</div>

				<!-- 관광지 리스트 End -->
			</div>
			<!-- 중앙 left content end -->
			<!-- 중앙 center content end -->
			<div class="col-md-9">
				<div class="alert alert-primary mt-3 text-center fw-bold"
					role="alert">전국 관광지 정보</div>
				<!-- 관광지 검색 start -->
				<form class="my-3" onsubmit="return false;" role="search">
					<div class="d-flex mb-3">
						<select id="select-sido" class="form-select me-2">
							<option value="0" selected>검색 할 시도 선택</option>
							<c:forEach var="sido" items="${sidoList}">
								<option value="${sido.sidoCode}">${sido.sidoName}</option>
							</c:forEach>
						</select> <select id="select-gugun" class="form-select me-2">
							<option value="0" selected>검색 할 구군 선택</option>
						</select> <input id="search-keyword" class="form-control me-2"
							type="search" placeholder="검색어" aria-label="검색어" />
						<button id="btn-search" class="btn btn-outline-success"
							type="button">검색</button>
					</div>
					<div class="d-flex" id="contentType">
						<label class="me-auto"><input
							class="search-content-class" type="radio" name="place-type"
							value="0" checked /> 전체 선택</label> <label
							class="me-auto"><input
							class="search-content-class" type="radio" name="place-type"
							value="12" /> 관광지</label> <label
							class="me-auto"><input
							class="search-content-class" type="radio" name="place-type"
							value="14" /> 문화시설</label> <label
							class="me-auto"><input
							class="search-content-class" type="radio" name="place-type"
							value="15" /> 축제공연행사</label> <label
							class="me-auto"><input
							class="search-content-class" type="radio" name="place-type"
							value="25" /> 여행코스</label> <label
							class="me-auto"><input
							class="search-content-class" type="radio" name="place-type"
							value="28" /> 레포츠</label> <label
							class="me-auto"><input
							class="search-content-class" type="radio" name="place-type"
							value="32" /> 숙박</label> <label
							class="me-auto"><input
							class="search-content-class" type="radio" name="place-type"
							value="38" /> 쇼핑</label> <label><input
							class="search-content-class" type="radio" name="place-type"
							value="39" /> 음식점</label>
					</div>
				</form>
				<!-- kakao map start -->
				<div id="map" class="mt-3" style="width: 100%; height: 400px"></div>
				<!-- kakao map end -->
			</div>
		</div>
	</div>
	<!-- 중앙 content end -->

	<!-- 여행 경로 추가 modal start -->
	<div class="modal fade" id="tripModal" data-bs-backdrop="static"
		data-bs-keyboard="false" tabindex="-1"
		aria-labelledby="staticBackdropLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">
						<i class="bi text-info"> 여행 경로 추가</i>
					</h4>
					<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">
					<form action="">
						<div class="row mb-3 mt-3">
							<div class="col-md-6">
								<i class="bi bi-calendar2-date text-primary"></i> <label
									for="start-date" class="form-label">시작일:</label> <input
									type="date" class="form-control" id="start-date"
									name="start-date" />
							</div>
							<div class="col-md-6">
								<i class="bi bi-calendar2-date text-danger"></i> <label
									for="end-date" class="form-label">종료일:</label> <input
									type="date" class="form-control" id="end-date" name="end-date" />
							</div>
						</div>
						<div class="mb-3">
							<i class="bi" style="color: rgb(121, 2, 119)"></i> <label
								for="question" class="form-label">여행제목:</label> <input
								type="text" class="form-control" id="question"
								placeholder="여행제목 입력..." name="question" />
						</div>
						<div class="mb-3">
							<i class="bi" style="color: rgb(14, 2, 121)"></i> <label
								for="question" class="form-label">세부일정:</label>
							<button type="button" id="btn-answer-add"
								class="btn btn-outline-primary btn-sm">
								<i class="bi bi-plus-square-fill"></i> 추가
							</button>
						</div>
						<div id="trip-answer-list" class="row mb-3">
							<div class="row mb-1 trip-answer-list-item">
								<div class="col-md-10">
									<input type="text" class="form-control" name="answer" />
								</div>
							</div>
						</div>
					</form>
				</div>

				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" id="btn-poll-create"
						class="btn btn-primary btn-sm">여행생성</button>
					<button type="button" class="btn btn-outline-danger btn-sm"
						data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 핫플레이스 추가 modal start -->
	<div class="modal fade" id="hotPlaceModal" data-bs-backdrop="static"
		data-bs-keyboard="false" tabindex="-1"
		aria-labelledby="staticBackdropLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">
						<i class="bi"> 핫플레이스 추가</i>
					</h4>
					<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">
					<form action="">
						<div class="mb-3">
							<i class="bi" style="color: rgb(121, 2, 119)"></i> <label
								for="question" class="form-label">핫 플레이스:</label> <input
								type="text" class="form-control" id="question"
								placeholder="핫 플레이스 입력..." name="question" />
						</div>
						<div class="mb-3">
							<i class="bi" style="color: rgb(14, 2, 121)"></i> <label
								for="question" class="form-label">리뷰:</label>
							<button type="button" id="btn-answer-add"
								class="btn btn-outline-primary btn-sm">
								<i class="bi bi-plus-square-fill"></i> 추가
							</button>
						</div>
						<div id="hotPlace-answer-list" class="row mb-3">
							<div class="row mb-1 hotPlace-answer-list-item">
								<div class="col-md-10">
									<input type="text" class="form-control" name="answer" />
								</div>
							</div>
						</div>
					</form>
				</div>

				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" id="btn-hotPlace-create"
						class="btn btn-primary btn-sm">
						리뷰작성</button>
					<button type="button" class="btn btn-outline-danger btn-sm"
						data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/views/include/footer.jsp"%>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
		crossorigin="anonymous"></script>
	<!-- <script src="./assets/js/main.js"></script> -->
	<script type="text/javascript"
		src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&libraries=services,clusterer,drawing"></script>
	<script type="text/javascript" src="/js/search.js"></script>
</body>
</html>
