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
			<%@ include file="/views/mypage/mypageNav.jsp" %>
			
			<!-- 메인 콘텐츠 -->
			<section class="mypage__content">
				<span class="content-header">${user.nickname}님의 냉장고</span>
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
		                    <span class="ingredient-tag active" data-ingredient-id="${ui.ingredientId}"
		                          data-ingredient-name="${ui.ingredient.name}">
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
						<c:choose>
							<c:when test="${not empty recommendMap}">
								<c:forEach var="entry" items="${recommendMap}">
									<a href="/mypage/recipe?action=detail&recipe_id=${entry.key.recipe_id}" class="recipe-card">
										<img src="${entry.key.thumbnail_image_url}" alt="${entry.key.title}" />
										<div class="card-overlay">
											<!-- 난이도 -->
<%--											<c:choose>--%>
<%--												<c:when test="${not empty entry.key.difficulty}">--%>
<%--													<c:choose>--%>
<%--														<c:when test="${entry.key.difficulty eq 'easy'}">--%>
<%--															<span class="difficulty easy">쉬움</span>--%>
<%--														</c:when>--%>
<%--														<c:when test="${entry.key.difficulty eq 'normal'}">--%>
<%--															<span class="difficulty normal">보통</span>--%>
<%--														</c:when>--%>
<%--														<c:when test="${entry.key.difficulty eq 'hard'}">--%>
<%--															<span class="difficulty hard">어려움</span>--%>
<%--														</c:when>--%>
<%--													</c:choose>--%>
<%--												</c:when>--%>
<%--											</c:choose>--%>
											
											<!-- 스크랩 버튼 -->
											<button class="scrap-btn ${scrapStatusMap[entry.key.recipe_id] ? 'active' : ''}"
											        data-recipe-id="${entry.key.recipe_id}">
												<i class="bookmark-icon"></i>
											</button>
											
											
											<!-- 제목 & 일치 재료 -->
											<h4>${entry.key.title}</h4>
											<p class="match">냉장고 재료 일치: ${entry.value}개</p>
										</div>
									</a>
								</c:forEach>
							</c:when>
							
							<c:otherwise>
								<p class="no-recipes">아직 추천할 레시피가 없어요 😅</p>
							</c:otherwise>
						</c:choose>
					</div>
				
				</div>
			</section>

			
		</div>
		
		<%-- 푸터 --%>
		<%@ include file="/views/components/footerComp.jsp"%>
		
		<script>
			// 스크랩 버튼을 추후에 주입하기 때문에 초반 dom은 부모에게 리스너 달고 동작하게 함.
			// 대신 스크랩 버튼일 때만 작동하게끔
            document.querySelector(".recipe-grid").addEventListener("click", (e) => {
                const btn = e.target.closest(".scrap-btn");
                if (!btn) return; // scrap-btn이 아니면 무시
                e.stopPropagation();  // 부모(a) 클릭 막기
                e.preventDefault();   // a 태그의 기본 이동도 막기


                const recipeId = btn.dataset.recipeId;
                const isActive = btn.classList.toggle("active");

                fetch(`/scrap`, {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: new URLSearchParams({
                        action: isActive ? "add" : "remove",
                        recipeId: recipeId
                    })
                })
                    .then(res => res.json())
                    .then(data => {
                        console.log("Scrap result:", data);
                    })
                    .catch(err => console.error("Scrap error:", err));
            });

			
            // 재료 클릭 이벤트 수정
            document.querySelectorAll('.ingredient-tag').forEach(tag => {
                tag.addEventListener('click', function () {
                    this.classList.toggle('active');

                    // active 재료 목록 수집
                    const activeIngredients = Array.from(document.querySelectorAll('.ingredient-tag.active'))
                        .map(tag => tag.dataset.ingredientName);

                    // AJAX 요청
                    fetch("/mypage/fridge/filter", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({ ingredients: activeIngredients })
                    })
                        .then(res => res.json())
                        .then(data => updateRecipes(data))
                        .catch(err => console.error("추천 갱신 실패:", err));
                });
            });

            // 레시피 목록 영역 업데이트
            function updateRecipes(data) {
                const container = document.querySelector(".recipe-grid");
                container.innerHTML = ""; // 초기화

                if (!data || !data.recommendMap || Object.keys(data.recommendMap).length === 0) {
                    container.innerHTML = `<p class="no-recipes">추천할 레시피가 없어요 😅</p>`;
                    return;
                }
                
                for (const [recipeJson, matchCount] of Object.entries(data.recommendMap)) {
                    const recipe = JSON.parse(recipeJson);
                    container.innerHTML += format(`
					    <a href="/mypage/recipe?action=detail&recipe_id={0}" class="recipe-card">
					      <img src="{1}" alt="{2}" />
					      <div class="card-overlay">
							<button class="scrap-btn {3}"
											data-recipe-id="{4}">
								<i class="bookmark-icon"></i>
							</button>
					        <h4>{5}</h4>
					        <p class="match">냉장고 재료 일치: {6}개</p>
					      </div>
					    </a>
					  `,recipe.recipe_id,
	                    recipe.thumbnail_image_url,
	                    recipe.title,
	                    data.scrapStatusMap[recipe.recipe_id] ? "active" : "",
	                    recipe.recipe_id,
	                    recipe.title,
	                    matchCount
                    );
                }
            }
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
            
            // util: 함수
            function format(str, ...args) {
                return str.replace(/{(\d+)}/g, (match, index) => args[index]);
            }
		</script>
	
	</body>
</html>
