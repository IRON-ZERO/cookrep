<%@page import="java.time.LocalDate"%>
<%@ page language="java"
  pageEncoding="UTF-8"
  isELIgnored="false"
%>
<%
  LocalDate localDate = LocalDate.now();
  int year = localDate.getYear();
  request.setAttribute("year", year);
 %>
  <footer>
    <div class="footer-header">
      <a href="/">
        <img alt="로고이미지" src="/assets/images/logos/logo_3.png" />
      </a>
      <div class="footer-header__right">
        <h4>CooKRep</h4>
        <span>Recipe, Fresh Taste, Successful!!</span>
      </div>
    </div>
    <p>&copy; ${year} CooKRep. All Rights reserved.</p>
  </footer>
