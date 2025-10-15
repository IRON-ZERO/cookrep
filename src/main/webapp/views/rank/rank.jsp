<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8" isELIgnored="false"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<link rel="stylesheet" href="/assets/css/style.css" />
<title>CooKRep | Rank</title>
</head>
<body>
  <%@ include file="/views/components/headerComp.jsp"%>
  <main>
    <div class="wrapper rank-page__cont">
      <h2>쿠크랩 랭킹 !</h2>
      <ul class="grid-cont">
        <c:forEach var="rank" items="${ranking}">
          <li class="rank-page__cont-listitem">
            <a href="#" title="${rank.title}">
              <article>
                <div>
                  <h3>${rank.title}</h3>
                  <div>
                    <span class="badge-pcount"
                      data-count="${rank.people_count}"
                    > ${rank.people_count} 인분</span>
                    <span class="badge-level badge-level-${rank.cookLevel}">
                      ${rank.cookLevel}</span>
                  </div>
                  <div>
                    <span> 조회수 ${rank.views} </span>
                    <span> 준비 ${rank.prep_time} 분</span>
                    <span> 요리 ${rank.cook_time} 분</span>
                    <span> ${rank.like} 좋아요</span>
                    <span> ${rank.kcal} kcal</span>
                  </div>
                </div>
                <button>
                  <img src="/assets/images/icons/book-mark-yellow.png"
                    alt="즐겨찾기"
                  />
                </button>
                <img alt="요리썸네일" src="${rank.thumbnail_image_url}">
              </article>
            </a>
          </li>
        </c:forEach>
      </ul>
    </div>
  </main>
  <%@ include file="/views/components/footerComp.jsp"%>
  <script type="text/javascript" src="/assets/js/header.js"></script>
</body>
</html>