<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.recipe.RecipeDTO" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>My Recipes</title>
    <style>
        .card-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }
        .card {
            width: 200px;
            border: 1px solid #ddd;
            border-radius: 10px;
            overflow: hidden;
            text-align: center;
            cursor: pointer;
            transition: transform 0.2s;
        }
        .card img {
            width: 100%;
            height: 150px;
            object-fit: cover;
        }
        .card:hover {
            transform: scale(1.05);
        }
        .card-title {
            padding: 10px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<h1>My Recipes</h1>

<div class="card-container">
    <%
        List<RecipeDTO> recipes = (List<RecipeDTO>) request.getAttribute("recipes");
        if (recipes != null) {
            for (RecipeDTO r : recipes) {
    %>
    <div class="card" onclick="location.href='/mypage/recipe?action=detail&recipe_id=<%= r.getRecipe_id() %>'">
        <img src="<%= r.getThumbnail_image_url() %>" alt="<%= r.getTitle() %>">
        <div class="card-title"><%= r.getTitle() %></div>
    </div>
    <%
        }
    } else {
    %>
    <p>No recipes found.</p>
    <%
        }
    %>
</div>

</body>
</html>
