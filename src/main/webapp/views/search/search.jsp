<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8" isELIgnored="false"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/views/template/head.jsp" %>
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
            <a href="/mypage/recipe?action=detail&recipe_id=${list.recipe_id}" title="<c:out value='${list.title}'/>">
              <article>
                <div>
                  <h3>${list.title}</h3>
                  <div>
                    <span class="badge-pcount" data-count="${list.people_count}"> ${list.people_count} 인분</span>
                    <span class="badge-level badge-level-${list.cookLevel}"> ${list.cookLevel}</span>
                  </div>
                  <div>
                 <p>
                    <span>조회수</span>
                    <span>${list.views}</span>
                 </p>
                 <p>
                    <span>준비</span>
                    <span>${list.prep_time} 분</span>
                 </p>
                 <p>
                    <span>요리</span>
                    <span>${list.cook_time} 분</span>
                 </p>
                 <p>
                    <span>좋아요</span>
                    <span>${list.like}</span>
                 </p>
                 <p>
                    <span>${list.kcal}</span>
                    <span>kcal</span>
                 </p>
                  </div>
                </div>
            <!--     <button>
                  <img src="/assets/images/icons/book-mark-yellow.png"
                    alt="즐겨찾기"
                  />
                </button> -->
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
