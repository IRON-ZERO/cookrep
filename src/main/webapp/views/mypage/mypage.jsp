<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="UTF-8">
		<title>${user.nickname}님의 프로필 | CookRep</title>
		<%@include file="/views/template/head.jsp"%>
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
							<img src="/assets/images/icons/profile_icon.png" alt="profile_icon">
							<p>프로필</p>
						</div>
					</a>
					<a href="/mypage/fridge">
						<div>
							<img src="/assets/images/icons/fridge_icon.png" alt="fridge_icon">
							<span>내 냉장고</span>
						</div>
						</a>
					<a href="/mypage/scrap">
						<div>
							<img src="/assets/images/icons/fridge_icon.png" alt="freezer_icon">
							<span>내 냉동고</span>
						</div>
					</a>
					<a href="/mypage/recipe">
						<div>
							<img src="" alt="">
							<span>내 레시피</span>
						</div>
					</a>
				</nav>
				<div class="logout"><a href="/logout">로그아웃</a></div>
			</aside>
			
			<!-- 메인 영역 -->
			<section class="mypage__content">
				<span class="content-header">${user.nickname}님의 Profile</span>
					<div class="user-info">
						<div class="profile-view-mode">
							<div class="info-header">
								<button class="edit-button" type="button" onclick="toggleEdit(this)">edit✏️</button>
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
					<div class="profile-edit-mode">
						<div class="info-header">
							<button class="edit-button" type="button" onclick="toggleEdit(this)">✏️</button>
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
							<form method="post" action="/mypage?action=updateprofile" class="info-edit-form">
								<div class="detail-info-wrapper">
									<div class="detail-info">
										<div class="detail-info-header">Personal Information</div>
										<div class="detail-info-body">
											
											<div class="body-item">
												<label for="firstName">이름(성)</label>
												<input id="firstName" name="firstName" placeholder="${user.firstName}"></input>
											</div>
											<div class="body-item">
												<label for="lastName">이름</label>
												<input id="lastName" name="lastName" placeholder="${user.lastName}"></input>
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
												<input id="country" name="country" placeholder="${user.country}"></input>
											</div>
											<div class="body-item">
												<label for="city">City</label>
												<input id="city" name="city" placeholder="${user.city}"></input>
											</div>
										</div>
									</div>
								</div>
								<div class="submit-wrapper">
									<button type="submit">수정</button>
									<button onclick="toggleEdit(this)">취소</button>
								</div>
							</form>
						</div>
					</div>
				</div>
				<div class="delete-account-wrapper">
					<button type="button" class="delete-account-btn" onclick="confirmDelete()">회원 탈퇴</button>
				</div>
			</section>
			
		</div>
		
		<%@ include file="/views/components/footerComp.jsp"%>
		<script>
            function toggleEdit(button) {
                const card = button.closest('.user-info');
                const view = card.querySelector('.profile-view-mode');
                const edit = card.querySelector('.profile-edit-mode');

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

            // function confirmDelete() {
            //     if (confirm("정말로 회원 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) {
            //
            //         fetch("/deleteAccount", {
            //             method: "POST"
            //         })
            //         .then(response => {
            //             if (response.ok) {
            //                 alert("회원 탈퇴가 완료되었습니다.");
            //                 window.location.href = "/logout";
            //             } else {
            //                 alert("회원 탈퇴 중 오류가 발생했습니다.");
            //             }
            //         })
            //         .catch(error => {
            //             console.error("Error:", error);
            //             alert("서버 통신 중 문제가 발생했습니다.");
            //         });
            //     }
            // }
            function confirmDelete() {
                if (!confirm("정말 삭제하시겠습니까?")) return;

                const form = document.createElement("form");
                form.method = "post";
                form.action = "/deleteAccount";

                form.innerHTML =
                    '<input type="hidden" name="userId" value="'+${user.id}+'">';

                document.body.appendChild(form);
                form.submit();
            }

            // 디자인을 위한 스크립트
            document.addEventListener("DOMContentLoaded", function () {
                // 현재 URL 경로 (/mypage, /mypage/fridge 등)
                const currentPath = window.location.pathname;
				console.log(currentPath);
                // 모든 nav 링크 가져오기
                const links = document.querySelectorAll("nav a");

                links.forEach(link => {
                    // href 속성값
                    const linkPath = link.getAttribute("href");

                    // 현재 URL이 href로 시작하면 active 적용
                    if (currentPath === linkPath || currentPath.startsWith(linkPath + "/")) {
                        link.classList.add("active");
                    } else {
                        link.classList.remove("active");
                    }
                });
            });
		</script>
	</body>
</html>
