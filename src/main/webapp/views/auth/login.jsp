<%@ page
  language="java"
  contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"
  isELIgnored="false"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">

  <%@ include file="/views/template/head.jsp" %>
  <title>CooKRep | 로그인</title>
</head>
<body>
  <%-- Container --%>
  <div class="wrapper auth-page">
    <div class="auth-page__bg login-page__bg"></div>
    <div class="cont glass-white auth-page__form--cont">
      <%-- LogoImg --%>
      <img alt="로고이미지" src="./assets/images/logos/logo_1.png">
      <%-- Login Mode --%>
      <div id="loginMode" class="login-page__mode">
        <button id="idLogin" type="button">ID</button>
        <button id="emailLogin" type="button">E-MAIL</button>
      </div>
      <%-- Form --%>
      <form id="loginForm" class="auth-page__form" action="/login?action=username-login" method="POST">
        <fieldset class="auth-page__form--fieldset">
          <legend>로그인</legend>
          <%-- Identify Name --%>
          <label id="loginIdLabel" for="loginIdInput">Username</label>
          <div class="login-page__input-id">
            <img alt="유저아이콘" src="/assets/images/icons/user-icon-1.png"/>
            <input id="loginIdInput" name="identifyName" type="text"
              placeholder="유저이름을 입력해주세요." minlength="5" maxlength="20" required
            />
          </div>
          <c:if test="${not empty error_nickname}">
            <small>${error_nickname}</small>
          </c:if>
          <c:if test="${not empty error_email}">
            <small>${error_email}</small>
          </c:if>
          
          <%-- Password --%>
          <label for="loginPwInput">Password</label>
          <div class="login-page__input-pw">
            <img alt="유저아이콘" src="/assets/images/icons/lock.png"/>
            <%-- min max 값  --%>
            <input id="loginPwInput" name="password" type="password" placeholder="비밀번호를 입력해주세요." required />
            <button type="button">            
              <img alt="유저아이콘" src="/assets/images/icons/closed-eyes.png"/>
            </button>
          </div>
          
          
          <button type="submit">로그인</button>
          <c:if test="${not empty error_result}">
            <small style="text-align: center">${error_result}</small>
          </c:if>
        </fieldset>
      </form>
      <%-- Seperate bar --%>
      <div class="auth-page__seperator">
        <hr />
        <span>or</span>
        <hr />
      </div>
      <%-- Go To Join--%>
      <p class="auth-page__link">
        <span> 아직 회원이 아니신가요? </span>
        <a href="/join">회원가입</a>
      </p>
    </div>
    <%-- end login form cont --%>
  </div>
  <script type="text/javascript" src="./assets/js/auth/login.js"></script>
</body>
</html>