<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"
  isELIgnored="false"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link
    href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&family=Sunflower:wght@300&display=swap"
    rel="stylesheet"
  >
  <link rel="stylesheet" href="/assets/css/style.css"/>
  <title>CooKRep | Home</title>
</head>
<body>
  <%@ include file="/views/components/headerComp.jsp"%>
  <main>
    <div class="wrapper home-page__cont">
      <h2>쿠크랩 PICK BEST 20</h2>
      <section class="home-page__pick-list-cont">
      <ul id="homePagePickList" class="home-page__pick-list">
        <c:forEach var="r" varStatus="status" items="${recipe}">
          <li>
            <a href="#">
              <article>
                <div>
                  <p>
                    <span> ${r.rcpWay2} </span>
                    <span> ${r.rcpPat2} </span>
                  </p>
                  <h3>${r.rcpNm}</h3>
                  <c:if test="${not empty r.hashTag}">
                    <span># ${r.hashTag} </span>
                  </c:if>
                  <span> ${status.index + 1} </span>
                </div>
                <img alt="음식이미지" src="${r.attFileNoMk}" />
              </article>
            </a>
          </li>
        </c:forEach>
      </ul>
      </section>
    </div>
  </main>
  <%@ include file="/views/components/footerComp.jsp"%>
  <script src="/assets/js/header.js"></script>
  <script src="/assets/js/home/home.js"></script>
</body>
</html>