<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
  <meta charset="UTF-8">
<%@ include file="/views/template/head.jsp" %>
  <title>CooKRep | 페이지를 찾을 수 없어요</title>
</head>
<body>
  <%@ include file="/views/components/headerComp.jsp"%>
  <main>
    <div class="wrapper not-found__cont">
      <h3>There is not Food!</h3>
      <h2>여기엔 먹을 만한 음식이 없어요.</h2>
      <a href="/">홈으로</a>
    </div>
  </main>
  <%@ include file="/views/components/footerComp.jsp"%>
  </body>
</html>