<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.recipe.RecipeDTO" %>
<%@ page import="dto.recipe.RecipeDTO.Step" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Recipe Detail</title>
    <link rel="stylesheet" type="text/css" href="/assets/css/recipe/recipeDetail.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&family=Sunflower:wght@300&display=swap"
            rel="stylesheet"
    >
    <link rel="stylesheet" href="/assets/css/style.css"/>

</head>
<body>
<jsp:include page="/views/components/headerComp.jsp" />

<%
    RecipeDTO recipe = (RecipeDTO) request.getAttribute("recipe");
    if (recipe == null) {
%>
<p>Recipe not found.</p>
<%
} else {
%>

<div class="recipe-container">
    <h1><%= recipe.getTitle() %></h1>
    <img class="main-image" src="<%= recipe.getThumbnail_image_url() %>" alt="Main Image">

    <div class="recipe-info">
        <p><strong>👥 인원:</strong> <%= recipe.getPeople_count() %>명</p>
        <p><strong>⏱ 준비 시간:</strong> <%= recipe.getPrep_time() %>분</p>
        <p><strong>🍳 조리 시간:</strong> <%= recipe.getCook_time() %>분</p>
        <p><strong>🔥 조회수:</strong> <%= recipe.getViews() %>회 |
            <strong>❤️ 좋아요:</strong> <%= recipe.getLike() %> |
            <strong>🍽 칼로리:</strong> <%= recipe.getKcal() %> kcal</p>
    </div>

    <div class="steps">
        <h2>조리순서 <span>Steps</span></h2>
        <%
            List<Step> steps = recipe.getSteps();
            if (steps != null) {
                for (Step step : steps) {
        %>
        <div class="step">
            <h3>Step <%= step.getStepOrder() %></h3>
            <p><%= step.getContents() %></p>
            <%
                if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) {
            %>
            <img src="<%= step.getImageUrl() %>" alt="Step Image">
            <%
                }
            %>
        </div>
        <%
                }
            }
        %>
    </div>

    <!-- Back / Edit / Delete 버튼 -->
    <div class="back-btn">
        <button onclick="location.href='/mypage/recipe?action=list'">Back to List</button>
        <button onclick="location.href='/mypage/recipe?action=edit&recipe_id=<%= recipe.getRecipe_id() %>'">수정</button>
        <button onclick="deleteRecipe('<%= recipe.getRecipe_id() %>')">삭제</button>
    </div>
</div>
<jsp:include page="/views/components/footerComp.jsp" />

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

<script src="/assets/js/header.js"></script>
</body>
</html>