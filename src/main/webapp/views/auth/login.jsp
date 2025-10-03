<%@ page
  language="java"
  contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="./assets/css/style.css"/>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&family=Sunflower:wght@300&display=swap" rel="stylesheet">
  <title>CooKRep | 로그인</title>
</head>
<body>
  <%-- Container --%>
  <div class="wrapper login-page">
    <div class="login-page__bg"></div>
    <div class="cont glass-white login-page__form--cont">
      <%-- LogoImg --%>
      <img alt="로고이미지" src="./assets/images/logos/logo_1.png">
      <%-- Login Mode --%>
      <div id="loginMode" class="login-page__mode">
        <button id="idLogin" type="button">ID</button>
        <button id="emailLogin" type="button">E-MAIL</button>
      </div>
      <%-- Form --%>
      <form id="loginForm" class="login-page__form" action="" method="POST">
        <fieldset class="login-page__form--fieldset">
          <legend hidden>로그인</legend>
          <label id="loginIdLabel" for="loginIdInput">Username</label>
          <%-- min max 값  --%>
          <input id="loginIdInput" name="nickname" type="text" placeholder="유저이름을 입력해주세요." required />
          <small >유저이름 방식이 잘못되었습니다.</small>
          <label for="loginPwInput">Password</label>
          <%-- min max 값  --%>
          <input id="loginPwInput" name="" type="text" placeholder="비밀번호를 입력해주세요." required />
          <small>비밀번호는 영문, 숫자, 특수문자('_','-')를 조합한 10자리 이상이여야만 합니다.</small>
          <button type="submit">로그인</button>
        </fieldset>
      </form>
      <%-- Seperate bar --%>
      <div class="login-page__seperator">
        <hr />
        <span>or</span>
        <hr />
      </div>
      <%-- Go To Join--%>
      <p class="login-page__link--join">
        <span> 아직 회원이 아니신가요? </span>
        <a href="/join">회원가입</a>
      </p>
    </div>
    <%-- end login form cont --%>
  </div>
  <script type="text/javascript" src="./assets/js/auth/login.js"></script>
</body>
</html>



   
        
        
      
   
      
    