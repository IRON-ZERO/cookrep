<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.recipe.RecipeDTO" %>
<%@ page import="dto.recipe.RecipeDTO.Step" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Recipe Detail</title>
    <!-- 외부 CSS 파일 -->
    <link rel="stylesheet" type="text/css" href="/assets/css/recipe/recipeDetail.css">
    <link rel="stylesheet" href="/assets/css/style.css"/>

</head>
<body class="recipe-body">
<%@ include file="/views/components/headerComp.jsp"%>
<%
    RecipeDTO recipe = (RecipeDTO) request.getAttribute("recipe");
    if (recipe == null) {
%>
<p style="text-align:center; color:#888;">Recipe not found.</p>
<%
} else {
%>

<div class="recipe-container">
    <h1 class="recipe-title"><%= recipe.getTitle() %></h1>
    <img class="recipe-main-image" src="<%= recipe.getThumbnail_image_url() %>" alt="Main Image">

    <div class="recipe-info">
        <p><strong>👥 인원:</strong> <%= recipe.getPeople_count() %>명</p>
        <p><strong>⏱ 준비 시간:</strong> <%= recipe.getPrep_time() %>분</p>
        <p><strong>🍳 조리 시간:</strong> <%= recipe.getCook_time() %>분</p>
        <p><strong>🔥 조회수:</strong> <%= recipe.getViews() %>회 |
            <strong>❤️ 좋아요:</strong> <%= recipe.getLike() %> |
            <strong>🍽 칼로리:</strong> <%= recipe.getKcal() %> kcal</p>
    </div>

    <div class="recipe-steps">
        <h2>조리순서 <span>Steps</span></h2>
        <%
            List<Step> steps = recipe.getSteps();
            if (steps != null) {
                for (Step step : steps) {
        %>
        <div class="recipe-step">
            <div class="recipe-step-left">
                <div class="recipe-step-number"><%= step.getStepOrder() %></div>
                <div class="recipe-step-text"><%= step.getContents() %></div>
            </div>
            <%
                if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) {
            %>
            <div class="recipe-step-right">
                <img src="<%= step.getImageUrl() %>" alt="Step Image">
            </div>
            <%
                }
            %>
        </div>
        <%
                }
            }
        %>
    </div>

    <div class="recipe-back-btn">
        <button onclick="location.href='/mypage/recipe?action=list'">목록으로</button>
        <button onclick="location.href='/mypage/recipe?action=edit&recipe_id=<%= recipe.getRecipe_id() %>'">수정</button>
        <button onclick="deleteRecipe('<%= recipe.getRecipe_id() %>')">삭제</button>
    </div>
</div>

<script>
    function deleteRecipe(recipeId) {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        fetch(`/mypage/recipe?action=delRecipe&recipe_id=${recipeId}`, {
            method: 'POST'
        })
            .then(res => {
                if (res.ok) {
                    alert("삭제 완료!");
                    location.href = "/mypage/recipe?action=list";
                } else {
                    alert("삭제 실패");
                }
            })
            .catch(err => {
                console.error(err);
                alert("삭제 중 오류 발생");
            });
    }
</script>

<%
    }
%>
<%@ include file="/views/components/footerComp.jsp"%>
</body>
</html>
