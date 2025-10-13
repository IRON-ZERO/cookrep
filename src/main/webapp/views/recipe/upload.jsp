<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Recipe Upload</title>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f8f9fa;
            padding: 40px;
            color: #333;
        }

        h2 {
            text-align: center;
            margin-bottom: 30px;
        }

        form {
            max-width: 800px;
            margin: 0 auto;
            background: #fff;
            border-radius: 12px;
            padding: 40px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        }

        h3 {
            margin-top: 30px;
            margin-bottom: 10px;
            font-size: 18px;
            color: #444;
        }

        input[type="text"],
        input[type="number"],
        textarea {
            width: 100%;
            padding: 10px 14px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 15px;
            box-sizing: border-box;
        }

        textarea {
            height: 100px;
            resize: none;
        }

        input[type="file"] {
            margin-top: 8px;
        }

        button {
            cursor: pointer;
            border: none;
            border-radius: 8px;
            padding: 10px 16px;
            font-size: 14px;
        }

        button[type="button"] {
            background-color: #007bff;
            color: white;
            transition: 0.2s;
        }

        button[type="button"]:hover {
            background-color: #0056b3;
        }

        #steps {
            margin-top: 20px;
        }

        .step {
            border: 1px solid #e0e0e0;
            border-radius: 10px;
            padding: 20px;
            background-color: #fafafa;
            margin-bottom: 20px;
        }

        .step h4 {
            color: #333;
            font-size: 16px;
            margin-bottom: 10px;
        }

        .step textarea {
            height: 80px;
        }

        .form-section {
            margin-bottom: 30px;
        }

        .btn-secondary {
            background-color: #6c757d;
            color: #fff;
        }

        .btn-secondary:hover {
            background-color: #565e64;
        }

        .submit-btn {
            display: block;
            width: 100%;
            background-color: #28a745;
            color: white;
            font-weight: bold;
            font-size: 16px;
            padding: 14px 0;
            margin-top: 30px;
        }

        .submit-btn:hover {
            background-color: #218838;
        }
    </style>
</head>
<%@ include file="/views/components/headerComp.jsp"%>

<body>
<h2>레시피 작성</h2>

<form id="recipeForm">
    <div class="form-section">
        <h3>레시피 제목</h3>
        <input type="text" id="recipeTitle" placeholder="제목을 입력해주세요">
    </div>

    <div class="form-section">
        <h3>메인 이미지 (필수)</h3>
        <input type="file" id="mainImage" name="mainImage">
    </div>

    <div class="form-section">
        <h3>인원 수 / 준비시간 / 조리시간</h3>
        <input type="number" id="peopleCount" placeholder="인원 수">
        <br><br>
        <input type="number" id="prepTime" placeholder="준비 시간 (분)">
        <br><br>
        <input type="number" id="cookTime" placeholder="조리 시간 (분)">
    </div>

    <div class="form-section">
        <h3>조리 단계</h3>
        <div id="steps"></div>
        <button type="button" class="btn-secondary" onclick="addStep()">+ 단계 추가</button>
    </div>

    <button type="button" class="submit-btn" onclick="submitRecipe()">레시피 저장</button>
</form>

<script>
    const userId = "<%= (String)session.getAttribute("userId") %>";
    if (!userId || userId === "null") {
        alert("로그인이 필요합니다.");
        window.location.href = "/login";
    }

    let stepCount = 0;

    function addStep() {
        stepCount++;
        const container = document.getElementById("steps");

        const stepDiv = document.createElement("div");
        stepDiv.className = "step";
        stepDiv.innerHTML = `
            <h4>Step ${stepCount}</h4>
            <label>내용</label><br>
            <textarea placeholder="조리 내용을 입력하세요" id="stepContent${stepCount}"></textarea><br><br>
            <label>이미지 (선택)</label><br>
            <input type="file" id="stepImage${stepCount}"><br>
        `;
        container.appendChild(stepDiv);
    }

    async function submitRecipe() {
        const fileNames = [];
        const now = Date.now();

        const mainFile = document.getElementById("mainImage").files[0];
        if (mainFile) {
            fileNames.push(`users/${userId}/recipes/${now}/main/${mainFile.name}`);
        }

        for (let i = 1; i <= stepCount; i++) {
            const stepFile = document.getElementById(`stepImage${i}`).files[0];
            if (stepFile) {
                fileNames.push(
                    `users/${userId}/recipes/${now}/steps/${String(i).padStart(2, '0')}_${stepFile.name}`
                );
            }
        }

        const presignResp = await fetch("/recipe/s3/postrecipe", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({fileNames})
        });
        const presignData = await presignResp.json();

        for (let i = 0; i < presignData.urls.length; i++) {
            const fileObj = presignData.urls[i];
            let file;
            if (i === 0) file = mainFile;
            else file = document.getElementById(`stepImage${i}`).files[0];

            if (file) {
                await fetch(fileObj.uploadUrl, {method: "PUT", body: file});
            }
        }

        const recipeData = {
            userId,
            title: document.getElementById("recipeTitle").value,
            peopleCount: parseInt(document.getElementById("peopleCount").value) || 0,
            prepTime: parseInt(document.getElementById("prepTime").value) || 0,
            cookTime: parseInt(document.getElementById("cookTime").value) || 0,
            steps: []
        };

        for (let i = 1; i <= stepCount; i++) {
            const content = document.getElementById(`stepContent${i}`).value;
            const stepFile = document.getElementById(`stepImage${i}`).files[0];
            if (stepFile) {
                recipeData.steps.push({
                    content,
                    imageUrl: `users/${userId}/recipes/${now}/steps/${String(i).padStart(2,'0')}_${stepFile.name}`
                });
            } else {
                recipeData.steps.push({ content, imageUrl: null });
            }
        }

        if (mainFile) {
            recipeData.mainImageUrl = `users/${userId}/recipes/${now}/main/${mainFile.name}`;
        }

        const registerResp = await fetch("/recipe/register", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(recipeData)
        });

        if (!registerResp.ok) {
            alert("레시피 등록 실패!");
            return;
        }

        const result = await registerResp.json();
        alert("레시피 등록 완료!");
        location.href = `/mypage/recipe?action=detail&recipe_id=${result.recipe_id}`;
    }
</script>

<script src="/assets/js/header.js">

</script>
</body>
</html>
