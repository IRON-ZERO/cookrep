<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="UTF-8">
		<title>${user.nickname}님의 냉장고 | CookRep</title>
		<%@include file="/views/template/head.jsp"%>
		<style>
            .ingredient-tag {
                display: inline-block;
                background: #f3f3f3;
                border-radius: 20px;
                padding: 6px 12px;
                margin: 5px;
                position: relative;
                transition: background 0.2s;
            }
            .ingredient-tag:hover {
                background: #e0e0e0;
            }
            .delete-btn {
                display: none;
                position: absolute;
                top: -5px;
                right: -5px;
                background: red;
                color: white;
                border: none;
                border-radius: 50%;
                width: 18px;
                height: 18px;
                font-size: 12px;
                cursor: pointer;
	            padding-top: 1px;
	            padding-left: 9px;
            }
            .ingredient-tag:hover .delete-btn {
                display: block;
            }
            .add-box {
                margin-top: 20px;
            }
            .add-box input {
                width: 220px;
                padding: 5px;
            }
            .add-box button {
                padding: 6px 10px;
                margin-left: 5px;
                cursor: pointer;
            }
		</style>
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
					<a href="/mypage">프로필</a>
					<a href="/mypage/freezer" class="active">내 냉장고</a>
					<a href="/mypage/scrap">내 냉동고</a>
				</nav>
				<div class="logout"><a href="/logout">로그아웃</a></div>
			</aside>
			
			<!-- 메인 콘텐츠 -->
			<main class="content">
				<section class="section freezer-section">
					<h2>${user.nickname}님의 냉장고</h2>
					<p class="desc">${user.nickname}님이 가지고 있는 재료들이에요.</p>
					
					<!-- 재료 목록 -->
					<div class="ingredients-box" id="ingredientsBox">
						<c:forEach var="ui" items="${user.ingredients}">
                    <span class="ingredient-tag">
                        ${ui.ingredient.name}
	                    <!-- 삭제 버튼 -->
                        <button class="delete-btn"
                                onclick="deleteIngredient('${user.id}','${ui.ingredientId}')">×</button>
                    </span>
						</c:forEach>
					</div>
					
					<!-- 재료 추가 -->
					<div class="add-box">
						<input type="text" id="ingredientInput" placeholder="예: 1,2,3,4">
						<button onclick="addIngredients('${user.id}')">추가</button>
					</div>
					
					<!-- 추천 레시피 카드 -->
					<div class="card">
						<h3>CookRep이 추천하는 레시피예요 🍳</h3>
						<div class="recipe-grid">
							<p class="placeholder">추천 레시피 데이터를 불러오는 중...</p>
						</div>
					</div>
				</section>
			</main>
		</div>
		
		<%@ include file="/views/components/footerComp.jsp"%>
		<script>
            // 재료 삭제 (폼 전송 방식)
            function deleteIngredient(userId, ingredientId) {
                if (!confirm("정말 삭제하시겠습니까?"+ingredientId)) return;

                const form = document.createElement("form");
                form.method = "post";
                form.action = "/mypage/freezer";

                form.innerHTML =
            '<input type="hidden" name="action" value="deleteingredient">'
            + '<input type="hidden" name="userId" value="'+userId+'">'
            + '<input type="hidden" name="ingredientId" value="'+ingredientId+'">';

                document.body.appendChild(form);
                form.submit(); // 컨트롤러 → deleteIngredient → getIngredients → freezer.jsp 다시 렌더링
            }

            // 재료 추가 (폼 전송 방식)
            function addIngredients(userId) {
                const input = document.getElementById("ingredientInput").value.trim();
                if (!input) {
                    alert("재료 ID를 입력하세요. 예: 1,2,3");
                    return;
                }

                const form = document.createElement("form");
                form.method = "post";
                form.action = "/mypage/freezer";

                form.innerHTML =
                    '<input type="hidden" name="action" value="addingredient">'
                    + '<input type="hidden" name="userId" value="'+userId+'">'
                    + '<input type="hidden" name="ingredientIds" value="'+input+'">';

                document.body.appendChild(form);
                form.submit(); // 컨트롤러 → addIngredient → getIngredients → freezer.jsp 다시 렌더링
            }
		</script>
	
	</body>
</html>
