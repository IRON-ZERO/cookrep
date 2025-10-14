<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.recipe.RecipeDTO" %>
<%@ page import="dto.recipe.RecipeDTO.Step" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Recipe Detail</title>
    <style>
        .recipe-container {
            max-width: 800px;
            margin: auto;
        }
        .main-image {
            width: 100%;
            max-height: 400px;
            object-fit: cover;
            border-radius: 10px;
        }
        .recipe-info {
            margin-top: 20px;
        }
        .steps {
            margin-top: 30px;
        }
        .step {
            margin-bottom: 20px;
        }
        .step img {
            width: 100%;
            max-height: 300px;
            object-fit: cover;
            border-radius: 10px;
        }
        .back-btn {
            margin-top: 20px;
        }
    </style>
</head>
<body>
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
        <p>Servings: <%= recipe.getPeople_count() %></p>
        <p>Prep Time: <%= recipe.getPrep_time() %> mins</p>
        <p>Cook Time: <%= recipe.getCook_time() %> mins</p>
        <p>Views: <%= recipe.getViews() %> | Likes: <%= recipe.getLike() %> | Calories: <%= recipe.getKcal() %></p>
    </div>

    <div class="steps">
        <h2>Steps</h2>
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
</body>
</html>
