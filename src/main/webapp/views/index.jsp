<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"
  isELIgnored="false"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <%@ include file="/views/template/head.jsp" %>
  <title>CooKRep | Home</title>
</head>
<body>
  <%@ include file="/views/components/headerComp.jsp"%>
  <main>
    <div class="wrapper home-page__cont">
      <h2>쿠크랩 PICK BEST 20</h2>
      <section class="home-page__pick-list-cont">
        <c:choose>
          <c:when test="${ empty recipe }">
            <h3>오늘 일일 공공데이터 사용량을 모두 소진하셨어요. 잠시 후 다시 시도해주세요.ㅠㅠ</h3>
          </c:when>
          <c:otherwise>
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
          </c:otherwise>
        </c:choose>
      </section>
    </div>
  </main>
  <%@ include file="/views/components/footerComp.jsp"%>
  <script src="/assets/js/header.js"></script>
  <script src="/assets/js/home/home.js"></script>
</body>
</html>