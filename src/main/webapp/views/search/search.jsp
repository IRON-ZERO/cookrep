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
<title>CooKRep | Search</title>
</head>
<body>
  <%@ include file="/views/components/headerComp.jsp"%>
  <main>
    <div class="wrapper search-page__cont">
      <h2>레시피 검색을 해보아요!</h2>
      <form  method="GET">
        <fieldset>
          <legend>레시피 검색</legend>
          <label for="searchInput">레시피 검색</label>
          <input id="searchInput" name="search-items" placeholder="레시피를 검색해보세요. ','를 사용해 여러 검색을 할 수 있답니다." />
          <button>검색</button>
        </fieldset>
      </form>
      <ul class="grid-cont">
        <c:forEach var="list" items="${defaultList}">
          <li class="search-page__cont-listitem">
            <a href="#" title="${list.title}">
              <article>
                <div>
                  <h3>${list.title}</h3>
                  <div>
                    <span class="badge-pcount" data-count="${list.people_count}"> ${list.people_count} 인분</span>
                    <span class="badge-level badge-level-${list.cookLevel}"> ${list.cookLevel}</span>
                  </div>
                  <div>
                    <span> 조회수 ${list.views} </span>
                    <span> 준비 ${list.prep_time} 분</span>
                    <span> 요리 ${list.cook_time} 분</span>
                    <span> ${list.like} 좋아요</span>
                    <span> ${list.kcal} kcal</span>
                  </div>
                </div>
                <button>
                  <img src="/assets/images/icons/book-mark-yellow.png"
                    alt="즐겨찾기"
                  />
                </button>
                <img alt="요리썸네일" src="${list.thumbnail_image_url}">
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
