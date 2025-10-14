<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="UTF-8">
		<title>${user.nickname}님의 냉장고 | CookRep</title>
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
			
			<!-- 메인 콘텐츠 -->
			<section class="mypage__content">
				<div class="content-header">
					<h3>${user.nickname}님의 냉장고</h3>
				</div>
				<div class="user-info">
					<div class="ingredient-info">
						<button class="edit-button" type="button" ></button>
						<div class="title-input-wrapper">
							<h3>${user.nickname}님이 가지고 있는 재료들이에요.</h3>
							<div>
								<div class="add-box">
									<input type="text" id="ingredientInput" placeholder="예) 당근,시금치">
									<button onclick="addIngredients('${user.id}')">추가</button>
								</div>
							</div>
						</div>
						<p>가지고 있는 재료를 누르면 메뉴를 추천해드려요.</p>
						<div class="ingredients-box" id="ingredientsBox">
						<c:forEach var="ui" items="${user.ingredients}">
		                    <span class="ingredient-tag" data-ingredient-id="${ui.ingredientId}">
		                        ${ui.ingredient.name}
			                    
			                    <!-- 삭제 버튼 -->
		                        <button class="delete-btn"
		                                data-user-id="${user.id}"
		                                data-ingredient-id="${ui.ingredientId}">×</button>
		                    </span>
						</c:forEach>
						</div>
					</div>
				</div>
				<div class="user-info recipe">
					<h3>CookRep이 추천하는 레시피에요.</h3>
					<div class="recipe-grid">
						<div class="recipe-card">
							<img src="/assets/images/recipe1.jpg" alt="레시피 썸네일" />
							<div class="card-overlay">
								<span class="difficulty normal">보통</span>
								<button class="scrap-btn ${recipe.scrapped ? 'active' : ''}">
									<i class="bookmark-icon"></i>
								</button>
								<h4>두부를 이용한 미친 스테이크 맛있겠다</h4>
							</div>
						</div>
						
						<div class="recipe-card">
							<img src="/assets/images/recipe2.jpg" alt="레시피 썸네일" />
							<div class="card-overlay">
								<span class="difficulty easy">쉬움</span>
								<button class="scrap-btn ${recipe.scrapped ? 'active' : ''}">
									<i class="bookmark-icon"></i>
								</button>
								<h4>무지 쉬운 라면으로 만드는 초간단 요리</h4>
							</div>
						</div>
						<div class="recipe-card">
							<img src="/assets/images/recipe2.jpg" alt="레시피 썸네일" />
							<div class="card-overlay">
								<span class="difficulty easy">쉬움</span>
								<button class="scrap-btn ${recipe.scrapped ? 'active' : ''}">
									<i class="bookmark-icon"></i>
								</button>
								<h4>무지 쉬운 라면으로 만드는 초간단 요리</h4>
							</div>
						</div>
						<div class="recipe-card">
							<img src="/assets/images/recipe2.jpg" alt="레시피 썸네일" />
							<div class="card-overlay">
								<span class="difficulty easy">쉬움</span>
								<button class="scrap-btn ${recipe.scrapped ? 'active' : ''}">
									<i class="bookmark-icon"></i>
								</button>
								<h4>무지 쉬운 라면으로 만드는 초간단 요리</h4>
							</div>
						</div>
						<div class="recipe-card">
							<img src="/assets/images/recipe2.jpg" alt="레시피 썸네일" />
							<div class="card-overlay">
								<span class="difficulty easy">쉬움</span>
								<button class="scrap-btn ${recipe.scrapped ? 'active' : ''}">
									<i class="bookmark-icon"></i>
								</button>
								<h4>무지 쉬운 라면으로 만드는 초간단 요리</h4>
							</div>
						</div>
						<div class="recipe-card">
							<img src="/assets/images/recipe2.jpg" alt="레시피 썸네일" />
							<div class="card-overlay">
								<span class="difficulty easy">쉬움</span>
								<button class="scrap-btn ${recipe.scrapped ? 'active' : ''}">
									<i class="bookmark-icon"></i>
								</button>
								<h4>무지 쉬운 라면으로 만드는 초간단 요리</h4>
							</div>
						</div>
						<div class="recipe-card">
							<img src="/assets/images/recipe2.jpg" alt="레시피 썸네일" />
							<div class="card-overlay">
								<span class="difficulty easy">쉬움</span>
								<button class="scrap-btn ${recipe.scrapped ? 'active' : ''}">
									<i class="bookmark-icon"></i>
								</button>
								<h4>무지 쉬운 라면으로 만드는 초간단 요리</h4>
							</div>
						</div>
						
					</div>
				</div>
			</section>
<%--			레시피 카드 반복문--%>
<%--			<div class="recipe-grid">--%>
<%--				<c:forEach var="recipe" items="${recommendList}">--%>
<%--					<div class="recipe-card">--%>
<%--						<img src="${recipe.thumbnailUrl}" alt="${recipe.title}" />--%>
<%--						<div class="card-overlay">--%>
<%--        <span class="difficulty ${recipe.difficulty == '쉬움' ? 'easy' : 'normal'}">--%>
<%--		        ${recipe.difficulty}--%>
<%--        </span>--%>
<%--							<h3>${recipe.title}</h3>--%>
<%--						</div>--%>
<%--					</div>--%>
<%--				</c:forEach>--%>
<%--			</div>--%>
			
		</div>
		
		<%-- 푸터 --%>
		<%@ include file="/views/components/footerComp.jsp"%>
		
		<script>
			// 스크랩 버튼 클릭시 토글 동작
            document.querySelectorAll(".scrap-btn").forEach((btn) => {
                btn.addEventListener("click", (e) => {
                    e.stopPropagation(); // 카드 클릭과 구분
                    btn.classList.toggle("active");
			
                    const recipeId = btn.dataset.recipeId;
                    const isScrapped = btn.classList.contains("active");
			
                    // 백엔드에 AJAX 요청
                <%--    fetch(`/scrap?action=${isScrapped ? 'add' : 'remove'}&id=${recipeId}`)--%>
                <%--        .then((res) => res.json())--%>
                <%--        .then((data) => console.log(data)--%>
                <%--        );--%>
                });
            });

            // 재료 클릭 시 토글 동작
            document.querySelectorAll('.ingredient-tag').forEach(tag => {
                tag.addEventListener('click', function (ev) {
                    // toggle 동작: 예) 선택/비활성 토글
                    this.classList.toggle('active');
                });
            });
			// 재료 삭제 버튼 click event 발생시
            document.querySelectorAll('.delete-btn').forEach(btn => {
                btn.addEventListener('click', (e) => {
                    e.stopPropagation();
                    const userId = btn.dataset.userId;
                    const ingredientId = btn.dataset.ingredientId;
                    deleteIngredient(userId, ingredientId);
                });
            });
			
            // 재료 삭제 (폼 전송 방식)
            function deleteIngredient(userId, ingredientId) {
                if (!confirm("정말 삭제하시겠습니까?")) return;

                const form = document.createElement("form");
                form.method = "post";
                form.action = "/mypage/fridge";

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
                    alert("재료를 입력하세요. 예: 시금치,마늘");
                    return;
                }

                const form = document.createElement("form");
                form.method = "post";
                form.action = "/mypage/fridge";

                form.innerHTML =
                    '<input type="hidden" name="action" value="addingredient">'
                    + '<input type="hidden" name="userId" value="'+userId+'">'
                    + '<input type="hidden" name="ingredients" value="'+input+'">';

                document.body.appendChild(form);
                form.submit(); // 컨트롤러 → addIngredient → getIngredients → freezer.jsp 다시 렌더링
            }
            
            // 디자인을 위한 스크립트
            document.addEventListener("DOMContentLoaded", function () {
                // 현재 URL 경로 (/mypage, /mypage/fridge 등)
                const currentPath = window.location.pathname;
                
                // 모든 nav 링크 가져오기
                const links = document.querySelectorAll("nav a");

                links.forEach(link => {
                    // href 속성값
                    const linkPath = link.getAttribute("href");

                    // 현재 URL이 href로 시작하면 active 적용
                    if (currentPath === linkPath) {
                        link.classList.add("active");
                    } else {
                        link.classList.remove("active");
                    }
                });
            });
		</script>
	
	</body>
</html>
