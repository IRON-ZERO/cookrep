<%@ page language="java"
  pageEncoding="UTF-8" isELIgnored="false"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="font__notosans">
  <div>
    <a href="/">
      <img alt="로고이미지" src="/assets/images/logos/logo_2.png" />
    </a>
    <nav id="globalNav">
      <ul>
        <li>
          <a href="/" class="nav-active">Home</a>
        </li>
        <li>
          <a href="/search">Search</a>
        </li>
        <li>
          <a href="/rank">Rank</a>
        </li>
        <li>
          <a href="/new-recipe">레시피 생성</a>
        </li>
      </ul>
    </nav>
    <nav>
      <c:if test="${empty sessionScope.userId}">
        <a href="/login">로그인</a>
      </c:if>
      <c:if test="${not empty sessionScope.userId}">
        <div>
          <a href="/mypage">아바타</a>
          <a href="/logout">로그아웃</a>
        </div>
      </c:if>

    </nav>
  </div>
</header>