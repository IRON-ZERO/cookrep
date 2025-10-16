<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${user.nickname}님의 냉동고</title>
		<%@include file="/views/template/head.jsp"%>
	</head>
	<body>
		<div class="mypage__layout">
			<!-- 사이드바 -->
			<%@ include file="/views/mypage/mypageNav.jsp" %>
			
			<!-- 메인 영역 -->
			<section class="mypage__content">
				<span class="content-header">${user.nickname}님의 냉장고</span>
				<div class="user-info recipe">
					<h3>${user.nickname}님이 스크랩한 레시피들이에요.</h3>
					<div class="recipe-grid">
						<c:choose>
							<c:when test="${not empty scrapedRecipes}">
								<c:forEach var="recipe" items="${scrapedRecipes}">
									<div class="recipe-card">
										<img src="${recipe.thumbnail_image_url}" alt="${recipe.title}" />
										<div class="card-overlay">
											
											<!-- 난이도 (선택적으로 표시, 없을 수도 있음) -->
<%--											<c:if test="${not empty recipe.difficulty}">--%>
<%--												<c:choose>--%>
<%--													<c:when test="${recipe.difficulty eq 'easy'}">--%>
<%--														<span class="difficulty easy">쉬움</span>--%>
<%--													</c:when>--%>
<%--													<c:when test="${recipe.difficulty eq 'normal'}">--%>
<%--														<span class="difficulty normal">보통</span>--%>
<%--													</c:when>--%>
<%--													<c:when test="${recipe.difficulty eq 'hard'}">--%>
<%--														<span class="difficulty hard">어려움</span>--%>
<%--													</c:when>--%>
<%--												</c:choose>--%>
<%--											</c:if>--%>
											
											<!-- 스크랩 버튼 -->
											<button class="scrap-btn active" data-recipe-id="${recipe.recipe_id}">
												<i class="bookmark-icon"></i>
											</button>
											
											<!-- 제목 -->
											<h4>${recipe.title}</h4>
											
											<!-- 작성자 / 조회수 / 좋아요 등 필요하면 추가 -->
											<div class="meta">
												<span class="views">조회수 ${recipe.views}</span>
												<span class="likes">좋아요 ${recipe.like}</span>
											</div>
										</div>
									</div>
								</c:forEach>
							</c:when>
							
							<c:otherwise>
								<p class="no-recipes">아직 스크랩한 레시피가 없어요 😅</p>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</section>
		
		</div>
		
		<%@ include file="/views/components/footerComp.jsp"%>
		<script>
            // 스크랩 버튼 클릭시 "active" 토글 및 동작
            document.querySelectorAll(".scrap-btn").forEach((btn) => {
                btn.addEventListener("click", (e) => {
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
            });
		</script>
	</body>
</html>