<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="/WEB-INF/views/include/header.jsp" %>
        <link href="${root}/assets/styles/main.css" rel="stylesheet" />
        <!-- <script src="/js/main.js" type="text/javascript"></script>
        <script src="/js/account.js" type="text/javascript"></script> -->
    </head>
    <body>
        <%@ include file="/WEB-INF/views/include/nav.jsp" %>

        <!-- 중앙 content start-->
        <section id="board-make" class="pt-3">
            <div class="container" data-aos="fade-up">
                <div class="section-header text-center">
                    <h2>나만의 핫플 자랑하기</h2>
                    <div class="contour"></div>
                </div>

                <div class="row">
                    <!-- kakao map start -->
                    <div id="hotplace-map" class="col" style="width: 100%; height: 550px"></div>
                    <script
                        type="text/javascript"
                        src="//dapi.kakao.com/v2/maps/sdk.js?appkey=e6f6d1ba0faa1e1b28897b8ec0e6ae8c&libraries=services,clusterer,drawing"
                    ></script>
                    <script type="text/javascript" src="/js/hotplace.js"></script>

                    <!-- kakao map end -->

                    <div class="col content-body">
                        <form action="">
                            <i class="bi bi-camera-fill" style="color: rgb(255, 0, 98)"
                                >스마트폰으로 찍은 사진을 올려주세요!</i
                            >
                            <div class="input-group mb-3">
                                <input type="file" class="form-control" id="inputImageFile" />
                            </div>

                            <div class="mb-3">
                                <label for="question" class="form-label">핫플 이름 </label>
                                <input
                                    type="text"
                                    class="form-control"
                                    id="board-title"
                                    name="title"
                                />
                            </div>

                            <div class="mb-3">
                                <i class="bi bi-calendar2-date text-primary"></i>
                                <label for="start-date" class="form-label">다녀온 날짜</label>
                                <input
                                    type="date"
                                    class="form-control"
                                    id="start-date"
                                    name="start-date"
                                />
                            </div>

                            <div class="mb-3">
                                <label for="question" class="form-label">장소유형 </label>
                                <select id="search-content-id" class="form-select me-2">
                                    <option value="0" selected>장소 유형</option>
                                    <option value="12">관광지</option>
                                    <option value="14">문화시설</option>
                                    <option value="15">축제공연행사</option>
                                    <option value="25">여행코스</option>
                                    <option value="28">레포츠</option>
                                    <option value="32">숙박</option>
                                    <option value="38">쇼핑</option>
                                    <option value="39">음식점</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="question" class="form-label">핫플 상세설명</label>
                                <textarea
                                    class="form-control"
                                    id="board-content"
                                    rows="5"
                                ></textarea>
                            </div>
                        </form>

                        <div class="text-center mb-3">
                            <button class="btn btn-outline-primary btn-sm" onclick="">등록</button>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- 중앙 content end -->

        <%@ include file="/WEB-INF/views/include/footer.jsp" %>
    </body>
</html>
