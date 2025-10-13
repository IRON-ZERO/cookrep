<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
List<String> arr = List.of("김치","깍두","얼갈이");
request.setAttribute("arr", arr);
%>
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
  </head>
  <body>
    <%@ include file="/views/components/headerComp.jsp"%>
  <main class="wrapper">
    <ul class="grid-cont">
      <c:forEach var="a" items="${arr}">
        <li>${a}</li>
      </c:forEach>
    </ul>
  </main>
  <%@ include file="/views/components/footerComp.jsp"%>
  <script type="text/javascript" src="/assets/js/header.js"></script>
  </body>
</html>