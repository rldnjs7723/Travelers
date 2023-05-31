<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
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
        <section id="board-share-list" class="pt-3">
            <div class="container" data-aos="fade-up">
                <div class="section-header text-center">
                    <h2>여행정보공유</h2>
                    <div class="contour"></div>
                </div>

                <div class="row align-self-center mb-2">
                    <div class="col-md-2 text-start">
                        <a
                            href="${root}/article?action=mvwrite"
                            id="btn-mv-register"
                            class="btn btn-outline-primary btn-sm"
                        >
                            글쓰기
                        </a>
                    </div>
                    <div class="col-md-7 offset-3">
                        <form
                            class="d-flex"
                            id="form-article-search"
                            action="POST"
                        >
                            <div class="input-group input-group-sm">
                                <input
                                    type="hidden"
                                    name="action"
                                    value="tripinfo"
                                />
                                <input
                                    type="hidden"
                                    name="order"
                                    value="none"
                                />
                                <input
                                    type="text"
                                    name="keyword"
                                    id="keyword"
                                    class="form-control"
                                    placeholder="검색어..."
                                    value="${keyword}"
                                />
                                <button
                                    id="btn-article-search"
                                    class="btn btn-dark"
                                    type="button"
                                    onclick="search()"
                                >
                                    검색
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <table class="table">
                    <thead>
                        <tr class="text-center">
                            <th scope="col">글번호</th>
                            <th scope="col">분류</th>
                            <th scope="col">
                                <c:if test="${order == 'none'}">
                                    <a
                                        href="${root}/article?action=tripinfo&order=asc&keyword=${keyword}"
                                        id="article-title-order"
                                        class="link-dark"
                                        style="text-decoration: none"
                                    >
                                        제목
                                    </a>
                                </c:if>
                                <c:if test="${order == 'asc'}">
                                    <a
                                        href="${root}/article?action=tripinfo&order=desc&keyword=${keyword}"
                                        id="article-title-order"
                                        class="link-dark"
                                        style="text-decoration: none"
                                    >
                                        제목 ↑
                                    </a>
                                </c:if>
                                <c:if test="${order == 'desc'}">
                                    <a
                                        href="${root}/article?action=tripinfo&order=asc&keyword=${keyword}"
                                        id="article-title-order"
                                        class="link-dark"
                                        style="text-decoration: none"
                                    >
                                        제목 ↓
                                    </a>
                                </c:if>
                            </th>
                            <th scope="col">작성자</th>
                            <th scope="col">조회수</th>
                            <th scope="col">좋아요</th>
                            <th scope="col">작성일</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="article" items="${articles}">
                            <tr class="text-center">
                                <td scope="row">${article.articleNo}</td>
                                <td scope="row">${article.articleType}</td>
                                <td class="text-start">
                                    <a
                                        <%-- href="${root}/article?action=tripinfoview&articleno=${article.articleNo}" --%>
                                        href="${root}/article/${article.articleNo}/${article.articleType}"
                                        class="article-title link-dark"
                                        data-code="${article.articleNo}"
                                        style="text-decoration: none"
                                    >
                                        ${article.title}
                                    </a>
                                </td>
                                <td>${article.userId}</td>
                                <td>${article.hit}</td>
                                <td>${article.like}</td>
                                <td>${article.writeTime}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>
        <!-- 중앙 content end -->
        <script>
        <%--
            document
                .querySelector("#btn-article-search")
                .addEventListener("click", function () {
                    let form = document.querySelector("#form-article-search");
                    let keyword = document.querySelector("#keyword").value;
                    form.setAttribute("action", "${root}/article");
                    form.submit();
                });
        --%>
        </script>

        <%@ include file="/WEB-INF/views/include/footer.jsp" %>
    </body>
</html>
