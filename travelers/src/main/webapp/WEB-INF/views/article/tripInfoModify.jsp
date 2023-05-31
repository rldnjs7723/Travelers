<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <%@ include file="/WEB-INF/views/include/header.jsp"%>
    <link href="${root}/assets/styles/main.css" rel="stylesheet" />
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
          <div class="row my-4"></div>
          <div class="py-5">
            <h2>게시물 수정</h2>
          </div>

          <div class="row g-5">
            <div class="col-12">
              <form class="needs-validation" id="form-modify" method="POST">
                <div class="row g-3">
                  <div class="col-12">
                  	<input type="hidden" name="articleno" value="${article.articleNo}">
                    <label for="title" class="form-label">제목</label>
                    <input
                      type="text"
                      class="form-control"
                      id="subject"
                      name="subject"
                      placeholder=""
                      value="${article.subject}"
                      required
                    />
                    <div class="invalid-feedback">제목을 입력해주세요.</div>
                  </div>

                  <div class="col-12">
                    <label for="content" class="form-label">내용</label>
                    <div class="input-group has-validation">
                      <textarea
                        class="form-control"
                        id="content"
                        name="content"
                        rows="10"
                      >${article.content}</textarea>
                      <div class="invalid-feedback">내용을 입력해주세요.</div>
                    </div>
                  </div>

                  <div class="col-12">
                    <label for="form-file" class="form-label">첨부파일</label>
                    <input class="form-control" type="file" id="form-file" />
                  </div>
                </div>

                <div class="col-12" style="text-align: end">
                  <a href="${root}/board?action=tripinfoview&articleno=${article.articleNo}" class="w-10 btn btn-outline-primary my-5" type="reset">취소</a>
                  <button type="button" class="w-10 btn btn-primary mx-1 my-5" id="btn-tripinfo-modify" >수정</button>
                </div>
              </form>
            </div>
          </div>
        </main>
      </div>
    </section>
    <!-- 중앙 content end -->

    <%@ include file="/WEB-INF/views/include/footer.jsp"%>
    <script>
      document.querySelector("#btn-tripinfo-modify").addEventListener("click", function () {
        if (!document.querySelector("#subject").value) {
          alert("제목 입력!!");
          return;
        } else if (!document.querySelector("#content").value) {
          alert("내용 입력!!");
          return;
        } else {
          let form = document.querySelector("#form-modify");
          form.setAttribute("action", "${root}/board?action=modify");
          form.submit();
        }
      });
    </script>
  </body>
</html>
