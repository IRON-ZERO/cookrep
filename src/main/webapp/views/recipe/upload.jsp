<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Recipe Upload</title>
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
<main>
    <div class="cont recipe-upload__cont">
        <h2>레시피 작성</h2>
        <form id="recipeForm">
            <div>
                <label for="recipeTitle" >레시피 제목</label>
                <input type="text" id="recipeTitle"  placeholder="제목을 입력해주세요"/>
            </div>

            <div>
                <label for="mainImage" >메인 이미지 (필수)</label>
                <input type="file" id="mainImage" name="mainImage">
            </div>

            <div>
                <label for="peopleCount" >인원 수 / 준비시간 / 조리시간</label>
                <input type="number" id="peopleCount"  placeholder="인원 수">
                <input type="number" id="prepTime"  placeholder="준비 시간 (분)">
                <input type="number" id="cookTime"  placeholder="조리 시간 (분)">
            </div>

            <div>
                <h3>조리 단계</h3>
                <div id="steps"></div>
                <button type="button" onclick="addStep()">+ 단계 추가</button>
            </div>

            <button type="button" onclick="submitRecipe()">레시피 저장</button>
        </form>
    </div>

</main>

<jsp:include page="/views/components/footerComp.jsp" />

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
            <label for="stepContent${stepCount}">내용</label>
            <textarea placeholder="조리 내용을 입력하세요" id="stepContent${stepCount}"></textarea>
            <label for="stepImage${stepCount}">이미지(선택)</label>
            <input type="file" id="stepImage${stepCount}">
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

<script src="/assets/js/header.js"></script>
</body>
</html>