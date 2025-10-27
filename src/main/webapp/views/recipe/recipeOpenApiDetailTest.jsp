<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>레시피 상세</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; background-color: #f5f5f5; padding: 20px; }
        .recipe-container { max-width: 800px; margin: 0 auto; background: #fff; padding: 20px; border-radius: 10px; }
        .recipe-title { font-size: 28px; font-weight: bold; margin-bottom: 15px; color: #F28C28; }
        .recipe-img { width: 100%; max-height: 350px; object-fit: cover; border-radius: 10px; margin-bottom: 20px; }
        .section { margin-bottom: 20px; }
        .section h3 { font-size: 18px; margin-bottom: 10px; color: #333; }
        .section p { margin: 5px 0; color: #555; }
        .manual-step { margin-bottom: 10px; }
        .manual-step img { width: 100%; max-height: 200px; object-fit: cover; margin-top: 5px; border-radius: 5px; }
    </style>
</head>
<body>

<%
    // RecipeDTO 대신 Map<String, String>으로 안전하게 처리
    Map<String, String> recipe = (Map<String, String>) request.getAttribute("recipe");
    if (recipe == null) {
%>
<p>레시피를 찾을 수 없습니다.</p>
<%
    } else {
%>

<div class="recipe-container">
    <div class="recipe-title"><%= recipe.get("name") %></div>
    <img class="recipe-img" src="<%= recipe.get("img") %>"
         alt="<%= recipe.get("name") %>"
         onerror="this.src='https://via.placeholder.com/800x350';" />

    <div class="section">
        <h3>기본 정보</h3>
        <p>조리법: <%= recipe.get("prepTime") %></p>
        <p>요리 종류: <%= recipe.get("cookTime") %></p>
        <p>칼로리: <%= recipe.get("kcal") %> kcal</p>
        <p>단백질: <%= recipe.get("protein") %> g</p>
        <p>태그: <%= recipe.get("tags") %></p>
        <p>해시태그: <%= recipe.get("hashtags") %></p>
        <p>TIP: <%= recipe.get("RCP_NA_TIP") %></p>
    </div>

    <div class="section">
        <h3>재료</h3>
        <p><%= recipe.get("RCP_PARTS_DTLS") %></p>
    </div>

    <div class="section">
        <h3>단계별 조리법</h3>
        <%
            for(int i=1; i<=20; i++) {
                String manual = recipe.get("MANUAL" + (i<10 ? "0"+i : i));
                String manualImg = recipe.get("MANUAL_IMG" + (i<10 ? "0"+i : i));
                if(manual != null && !manual.trim().isEmpty()) {
        %>
        <div class="manual-step">
            <p><strong>Step <%= i %>:</strong> <%= manual %></p>
            <% if(manualImg != null && !manualImg.trim().isEmpty()) { %>
            <img src="<%= manualImg %>" alt="Step <%= i %> 이미지"
                 onerror="this.src='https://via.placeholder.com/400x200';" />
            <% } %>
        </div>
        <%
                }
            }
        %>
    </div>
</div>

<% } %>
</body>
</html>
