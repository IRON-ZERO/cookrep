<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="UTF-8">
		<title>${user.nickname}님의 프로필 | CookRep</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link
				href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&family=Sunflower:wght@300&display=swap"
				rel="stylesheet"
		>
		<link rel="stylesheet" href="/assets/css/mypage/mypage.css">
		<link rel="stylesheet" href="/assets/css/style.css"/>
	</head>
	<body>
		<div class="mypage__layout">
			<!-- 사이드바 -->
			<aside class="mypage__navigation">
				<a href="/">
					<img alt="로고이미지" src="/assets/images/logos/logo_1.png" />
				</a>
				<div class="profile-box">
					<img src="/assets/images/icons/user-icon-1.png" alt="profile" class="profile-img">
					<h3>${user.nickname}</h3>
					<p>${user.email}</p>
				</div>
				<nav>
					<a href="/mypage" class="active">
						<div>
							<img src="assets/images/icons/profile_icon.png" alt="profile_icon">
							<p>프로필</p>
						</div>
					</a>
					<a href="/mypage/fridge">
						<div>
							<img src="assets/images/icons/fridge_icon.png" alt="fridge_icon">
							<p>내 냉장고</p>
						</div>
						</a>
					<a href="/mypage/scrap">
						<div>
							<img src="assets/images/icons/fridge_icon.png" alt="freezer_icon">
							<p>내 냉동고</p>
						</div>
					</a>
				</nav>
				<div class="logout"><a href="/logout">로그아웃</a></div>
			</aside>
			
			<!-- 메인 영역 -->
			<section class="mypage__content">
				<span class="content-header">${user.nickname}님의 Profile</span>
				<div class="user-info">
					<div class="info-header">
						<a href="">✏️</a>
					</div>
					<div class="info-body">
						<div class="info-view-wrapper">
							<div class="info-view-image">
								<img src="/assets/images/icons/user-icon-1.png" alt="profile_picture">
							</div>
							<div class="info-view-context">
								<h3>${user.nickname}</h3>
								<span>${user.email}</span>
							</div>
						</div>
						<div class="detail-info-wrapper">
							<div class="detail-info">
								<div class="detail-info-header">Personal Information</div>
								<div class="detail-info-body">
									<div class="body-item">
										<label for="firstName">이름(성)</label>
										<span id="firstName">${user.firstName}</span>
									</div>
									<div class="body-item">
										<label for="lastName">이름</label>
										<span id="lastName">${user.lastName}</span>
									</div>
									<div class="body-item">
										<label for="email">이메일</label>
										<span id="email">${user.email}</span>
									</div>
								</div>
							</div>
							<div class="detail-info">
								<div class="detail-info-header">Address</div>
								<div class="detail-info-body">
									<div class="body-item">
										<label for="country">Country</label>
										<span id="country">${user.country}</span>
									</div>
									<div class="body-item">
										<label for="city">City</label>
										<span id="city">${user.city}</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
		
		<%@ include file="/views/components/footerComp.jsp"%>
		<script>
            function toggleEdit(button) {
                const card = button.closest('.card');
                const view = card.querySelector('.view-mode');
                const edit = card.querySelector('.edit-mode');

                if (view.style.display === "none") {
                    // 편집모드 종료
                    view.style.display = "block";
                    edit.style.display = "none";
                } else {
                    // 편집모드 시작
                    view.style.display = "none";
                    edit.style.display = "block";
                }
            }
		</script>
	</body>
</html>
