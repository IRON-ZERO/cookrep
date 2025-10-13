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
      <form action="" method="GET">
        <fieldset>
          <legend>레시피 검색</legend>
          <input name="" placeholder="레시피를 검색해보세요."/>
          <button>검색</button>
        </fieldset>
      </form>
      <ul class="grid-cont">
        <li style="background-color: red;"></li>
        <li style="background-color: cornflowerblue;"></li>
        <li style="background-color: teal;"></li>
        <li style="background-color: orange;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li>
        <li style="background-color: red;"></li> 
      </ul>
    </div>
  </main>
  <%@ include file="/views/components/footerComp.jsp"%>
  <script type="text/javascript" src="/assets/js/header.js"></script>
</body>
</html>
