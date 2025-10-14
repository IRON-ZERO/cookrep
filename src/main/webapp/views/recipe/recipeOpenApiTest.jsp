<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>CookRep 레시피</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            background-color: #f5f5f5;
        }

        /* 상단바 */
        .navbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 30px;
            background-color: #ffffff;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .navbar .logo {
            font-size: 24px;
            font-weight: bold;
            color: #F28C28;
        }
        .navbar .menu a {
            margin-left: 20px;
            text-decoration: none;
            color: #333;
            font-weight: 500;
        }

        /* 검색창 */
        .search-section {
            margin: 30px auto;
            width: 90%;
            max-width: 800px;
        }
        .search-section input[type="text"] {
            width: 80%;
            padding: 10px 15px;
            border-radius: 20px 0 0 20px;
            border: 1px solid #ccc;
            outline: none;
            font-size: 14px;
        }
        .search-section button {
            padding: 10px 20px;
            border: none;
            background-color: #b5cbb7;
            border-radius: 0 20px 20px 0;
            cursor: pointer;
            font-size: 14px;
            color: #333;
        }

        /* 카드형 레시피 리스트 */
        .recipe-container {
            display: grid;
            grid-template-columns: repeat(4, 1fr); /* 한 줄에 4개 카드 */
            gap: 20px;
            width: 90%;
            margin: 20px auto 50px;
        }
        .recipe-card {
            background-color: #fff;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            transition: transform 0.2s;
        }
        .recipe-card:hover {
            transform: translateY(-5px);
        }
        .recipe-card img {
            width: 100%;
            height: 180px;
            object-fit: cover;
        }
        .recipe-content {
            padding: 10px 15px;
        }
        .recipe-title {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 5px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .recipe-hashtags {
            font-size: 12px;
            color: #777;
            margin-bottom: 8px;
        }
        .recipe-nutrition p {
            display: inline-block;
            margin-right: 10px;
            font-size: 12px;
            color: #555;
        }
    </style>
</head>
<body>

    <!-- 상단바 -->
    <div class="navbar">
        <div class="logo">CookRep</div>
        <div class="menu">
            <a href="#">Home</a>
            <a href="#">Search</a>
            <a href="#">Rank</a>
            <a href="#">Log out</a>
        </div>
    </div>

    <!-- 검색창 -->
    <div class="search-section">
        <form action="/recipes" method="get">
            <input type="text" name="keyword" placeholder="재료나 레시피 제목 키워드를 입력해보세요!" />
            <button type="submit">검색</button>
        </form>
    </div>

    <!-- 레시피 카드 리스트 -->
    <div class="recipe-container">
        <c:forEach var="recipe" items="${recipeList}">
            <!-- 클릭 시 name만 전달 -->
            <div class="recipe-card"
                 onclick="location.href='/recipes?action=detail&name=${recipe.name}'">
                <img src="${recipe.img}" alt="${recipe.name}" onerror="this.src='https://via.placeholder.com/220x150';" />
                <div class="recipe-content">
                    <div class="recipe-title">${recipe.name}</div>
                    <div class="recipe-hashtags">${recipe.hashtags}</div>
                    <div class="recipe-nutrition">
                        <p>칼로리: ${recipe.kcal} kcal</p>
                        <p>단백질: ${recipe.protein} g</p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>



</body>
</html>
