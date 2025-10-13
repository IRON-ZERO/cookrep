<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="UTF-8">
		<title>${user.nickname}님의 프로필 | CookRep</title>
		<link rel="stylesheet" href="/assets/css/mypage/style.css">
		<link rel="stylesheet" href="/assets/css/style.css"/>
	</head>
	<body>
		<%@ include file="/views/components/headerComp.jsp"%>
		<div class="layout">
			<!-- 사이드바 -->
			<aside class="sidebar">
				<div class="profile-box">
					<img src="https://via.placeholder.com/100" alt="profile" class="profile-img">
					<h3>${user.nickname}</h3>
					<p>${user.email}</p>
				</div>
				<nav class="menu">
					<a href="/mypage" class="active">프로필</a>
					<a href="/mypage/freezer">내 냉장고</a>
					<a href="/mypage/scrap">내 냉동고</a>
				</nav>
				<div class="logout"><a href="/logout">로그아웃</a></div>
			</aside>
			
			<!-- 메인 영역 -->
			<main class="content">
				<section class="section profile-section">
					<h2>${user.nickname}님의 Profile</h2>
					
					<div class="card">
						<div class="card-header">
							<h3>기본 정보</h3>
							<button type="button" class="edit-btn" onclick="toggleEdit(this)">Edit ✏️</button>
						</div>
						
						<div class="card-body">
							<!-- 보기 모드 -->
							<div class="view-mode">
								<div class="user-info">
									<img src="https://via.placeholder.com/80" alt="profile" class="profile-img">
									<div>
										<h4>${user.nickname}</h4>
										<p>${user.email}</p>
										<p>${user.firstName} ${user.lastName}</p>
										<p>${user.city}, ${user.country}</p>
									</div>
								</div>
							</div>
							
							<!-- 수정 모드 -->
							<form class="edit-mode" action="/mypage" method="post" style="display:none;">
								<input type="hidden" name="action" value="updateprofile">
								<div class="user-info-edit">
									<img src="https://via.placeholder.com/80" alt="profile" class="profile-img">
									<h4>${user.nickname}</h4>
									<p>${user.email}</p>
									<div class="input-group">
										<label>이름</label>
										<input type="text" name="firstName" value="${user.firstName}">
										<input type="text" name="lastName" value="${user.lastName}">
									</div>
									<div class="input-group">
										<label>도시 / 국가</label>
										<input type="text" name="city" value="${user.city}">
										<input type="text" name="country" value="${user.country}">
									</div>
								</div>
								<div class="edit-buttons">
									<button type="submit" class="save-btn">저장</button>
									<button type="button" class="cancel-btn" onclick="toggleEdit(this)">취소</button>
								</div>
							</form>
						</div>
					</div>
				
				
				<div class="card">
						<div class="card-header">
							<h3>Personal Information</h3>
						</div>
						<div class="card-body">
							<table class="info-table">
								<tr>
									<th>이름(성)</th><td>${user.firstName}</td>
									<th>이름</th><td>${user.lastName}</td>
								</tr>
								<tr>
									<th>이메일</th><td>${user.email}</td>
								</tr>
							</table>
						</div>
					</div>
					
					<div class="card">
						<div class="card-header">
							<h3>Address</h3>
						</div>
						<div class="card-body">
							<table class="info-table">
								<tr>
									<th>Country</th><td>${user.country}</td>
									<th>City</th><td>${user.city}</td>
								</tr>
							</table>
						</div>
					</div>
					
					<div class="delete-btn-area">
						<button class="delete-btn">회원탈퇴</button>
					</div>
				</section>
			</main>
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
