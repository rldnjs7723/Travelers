<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <%@ include file="/WEB-INF/views/include/header.jsp"%>
    <style>
      a {
        text-decoration: none;
      }
    </style>
    <link href="/assets/styles/main.css" rel="stylesheet" />
    <script src="/js/account.js" type="text/javascript"></script>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/include/nav.jsp"%>

    <section id="board-share-list" class="pt-3">
      <div class="container" data-aos="fade-up">
        <div class="section-header text-center">
          <h2>마이페이지</h2>
          <div class="contour"></div>
        </div>

        <div id="mypage-default" class="row justify-content-center">
          <div class="card mb-3 my-5" style="max-width: 50em; height: 32em">
            <div class="row g-0">
              <div class="col-md-4">
                <img
                  src="${root}/assets/img/common/profile.png"
                  class="img-fluid rounded-start"
                  alt="..."
                />
              </div>
              <div class="col-md-8 py-3">
                <div class="card-body">
                  <h5 class="card-title">자기소개</h5>
                  <p class="card-text">안녕하세요~ :)</p>
                  <p class="card-text">
                    <small class="text-muted">Last updated 3 mins ago</small>
                  </p>
                </div>
                <ul class="list-group list-group-flush">
                  <li class="list-group-item">
                    <div class="my-2"><small class="text-muted">이름</small><br /></div>
                    ${loginInfo.name}
                  </li>
                  <li class="list-group-item">
                    <div class="my-2"><small class="text-muted">아이디</small><br /></div>
                    ${loginInfo.id}
                  </li>
                  <li class="list-group-item">
                    <div class="my-2"><small class="text-muted">이메일</small><br /></div>
                    ${loginInfo.email}
                  </li>
                  <li class="list-group-item"></li>
                </ul>
                <div class="card-body" style="text-align: end">
                  <button onclick="withdraw()" class="btn btn-outline-danger">회원 탈퇴</button>
                  <button onclick="update()" class="btn btn-primary">정보 수정</button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div style="display: none" id="mypage-active" class="row justify-content-center">
          <div class="card mb-3 my-5" style="max-width: 50em; height: 32em">
            <div class="row g-0">
              <div class="col-md-4">
                <img
                  src="./assets/img/common/profile.png"
                  class="img-fluid rounded-start"
                  alt="..."
                />
              </div>
              <div class="col-md-8 py-3">
                <div class="card-body">
                  <h5 class="card-title">자기소개</h5>
                  <p class="card-text">안녕하세요~ :)</p>
                  <p class="card-text">
                    <small class="text-muted">Last updated 3 mins ago</small>
                  </p>
                </div>
                <form method="" id="modify-form">
                  <ul class="list-group list-group-flush">
                    <li class="list-group-item">
                      <div class="my-2"><small class="text-muted">이름</small><br /></div>
                      <input type="text" id="modify-name" name="name" value="${loginInfo.name}" />
                    </li>
                    <li class="list-group-item">
                      <div class="my-2"><small class="text-muted">아이디</small><br /></div>
                      <input type="text" id="modify-id" name="id" value="${loginInfo.id}" />
                    </li>
                    <li class="list-group-item">
                      <div class="my-2"><small class="text-muted">이메일</small><br /></div>
                      <input type="email" name="email" value="${loginInfo.email}" readonly />
                    </li>

                    <li class="list-group-item"></li>
                  </ul>
                </form>
                <div class="card-body" style="text-align: end">
                  <button onclick="update()" class="btn btn-danger">취소</button>
                  <button onclick="modify()" class="btn btn-primary" id="btn-modify">완료</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </body>
</html>
