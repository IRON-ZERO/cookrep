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
			<%@ include file="/views/mypage/mypageNav.jsp" %>
			
			
			<!-- 메인 영역 -->
			<section class="mypage__content">
				<span class="content-header">${user.nickname}님의 Profile</span>
					<div class="user-info">
						<div class="profile-view-mode">
							<div class="info-header">
								<button class="edit-button" type="button">edit✏️</button>
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
										<button class="edit-button">취소</button>
									</div>
								</form>
							</div>
						</div>
					</div>
				<div class="delete-account-wrapper">
					<button type="button" class="delete-account-btn">회원 탈퇴</button>
				</div>
			</section>
			
		</div>
		
		<%@ include file="/views/components/footerComp.jsp"%>
		<script>
            document.addEventListener("DOMContentLoaded", () => {
                document.addEventListener("click", (e) => {
                    // edit 버튼 클릭 처리
                    if (e.target.closest(".edit-button")) {
                        const button = e.target.closest(".edit-button");
                        const card = button.closest(".user-info");
                        const view = card.querySelector(".profile-view-mode");
                        const edit = card.querySelector(".profile-edit-mode");

                        const isEditing = view.style.display === "none";
                        view.style.display = isEditing ? "block" : "none";
                        edit.style.display = isEditing ? "none" : "block";
                    }

                    // 회원 탈퇴 버튼 클릭 처리
                    if (e.target.closest(".delete-account-btn")) {
                        if (!confirm("정말 삭제하시겠습니까?")) return;

                        const form = document.createElement("form");
                        form.method = "post";
                        form.action = "/deleteAccount";
                        document.body.appendChild(form);
                        form.submit();
                    }
                });
            });
            function format(str, ...args) {
                return str.replace(/{(\d+)}/g, (match, index) => args[index]);
            }

            <%--function toggleEdit(button) {--%>
            <%--    const card = button.closest('.user-info');--%>
            <%--    const view = card.querySelector('.profile-view-mode');--%>
            <%--    const edit = card.querySelector('.profile-edit-mode');--%>
			
            <%--    if (view.style.display === "none") {--%>
            <%--        // 편집모드 종료--%>
            <%--        view.style.display = "block";--%>
            <%--        edit.style.display = "none";--%>
            <%--    } else {--%>
            <%--        // 편집모드 시작--%>
            <%--        view.style.display = "none";--%>
            <%--        edit.style.display = "block";--%>
            <%--    }--%>
            <%--}--%>
            
            <%--function confirmDelete() {--%>
            <%--    if (!confirm("정말 삭제하시겠습니까?")) return;--%>
			
            <%--    const form = document.createElement("form");--%>
            <%--    form.method = "post";--%>
            <%--    form.action = "/deleteAccount";--%>
			
            <%--    form.innerHTML =--%>
	        <%--        format('<input type="hidden" name="userId" value="{0}">',${user.id});--%>
			
                // document.body.appendChild(form);
            <%--    form.submit();--%>
            <%--}--%>
            
		</script>
	</body>
</html>
