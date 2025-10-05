<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8" isELIgnored="false"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="./assets/css/style.css" />
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
   <link
  href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&family=Sunflower:wght@300&display=swap"
  rel="stylesheet"
  >
  <title>CooKRep | 회원가입</title>
</head>
<body>
  <%-- Container --%>
  <div class="wrapper auth-page">
    <div class="auth-page__bg join-page__bg"></div>
    <section class="cont glass-white auth-page__form--cont">
      <%-- LogoImg --%>
      <img alt="로고이미지" src="./assets/images/logos/logo_1.png">
      <%-- Form --%>
      <form id="joinForm" class="auth-page__form" action="/join" method="POST">
        <fieldset class="auth-page__form--fieldset">
          <legend hidden>회원가입</legend>
          <%-- username --%>
          <label for="joinNicknameInput">Username</label>
          <%-- min max 값  --%>
          <input id="joinNicknameInput" name="nickname" type="text"
            placeholder="유저이름을 입력해주세요." value="${nickname}" required
          />
          <c:if test="${not empty error_nickname}">
            <small>${error_nickname}</small>
          </c:if>
          <%-- email --%>
          <label for="joinEmailnput">Email</label>
          <%-- min max 값  --%>
          <input id="joinEmailnput" name="email" type="email"
            placeholder="이메일을 입력해주세요." value="${email}" required
          />
          <c:if test="${not empty error_email}">
            <small>${error_email}</small>
          </c:if>
          <%-- firstname --%>
          <label for="joinFirstNameInput">이름(성)</label>
          <%-- min max 값  --%>
          <input id="joinFirstNameInput" name="first_name" type="text"
            placeholder="이름(성)을 입력해주세요." value="${first_name}" required
          />
          <%-- lastname --%>
          <label for="joinLastNameInput">이름</label>
          <%-- min max 값  --%>
          <input id="joinLastNameInput" name="last_name" type="text"
            placeholder="이름을 입력해주세요." value="${last_name}" required
          />
          <%-- Country & City --%>
          <div class="join-page__addressBtn-cont">
            <div class="join-page__addressBtn-cont--header">
              <label for="joinCountryInput">주소</label>
              <div>
                <input id="joinAddressSelfCheckbox" type="checkbox"
                  aria-label="주소직접입력"
                />
                <label id="joinAddressSelfLabel"
                  class="join-page__address-cont--checkbox"
                  for="joinAddressSelfCheckbox"
                >
                  <span>직접입력</span>
                  <span class="join-page__address-cont--checkbox-bg">
                    <span class="join-page__address-cont--checkbox-box"></span>
                  </span>
                </label>
              </div>
            </div>
            <div>
              <input id="joinCountryInput" name="country" type="text"
                placeholder="국적" value="${country}" disabled
              />
              <input id="joinCityInput" name="city" type="text"
                placeholder="도시/구" value="${city}" disabled
              />
              <button id="joinAddressBtn" type="button">주소찾기</button>
            </div>
          </div>
          <%-- password --%>
          <label for="joinPwInput">비밀번호</label>
          <%-- min max 값  --%>
          <input id="joinPwInput" name="password" type="text"
            placeholder="비밀번호를 입력해주세요." value="${password}" required
          />
          <c:if test="${not empty error_password}">
            <small>${error_password}</small>
          </c:if>
          <%-- password check --%>
          <label for="joinCheckPassInput">2차확인 비밀번호</label>
          <%-- min max 값  --%>
          <input id="joinCheckPassInput" name="check_pass" type="text"
            placeholder="2차확인 비밀번호를 입력해주세요." value="${check_pass}" required
          />
          <button type="submit">회원가입</button>
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
        <span> 이미 회원이 이신가요? </span>
        <a href="/login">로그인</a>
      </p>
    </section>
    <%-- end login form cont --%>
  </div>
  <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"
    defer
  ></script>
  <script type="text/javascript" src="./assets/js/auth/join.js" defer></script>
</body>
</html>