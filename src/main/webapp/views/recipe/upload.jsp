<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Recipe Upload</title>
</head>
<body>
<h2>레시피 작성</h2>

<form id="recipeForm">
    <h3>레시피 제목</h3>
    <input type="text" id="recipeTitle" placeholder="레시피 제목 입력"><br><br>

    <h3>메인 이미지</h3>
    <input type="file" id="mainImage" name="mainImage"><br><br>

    <h3>인원 수</h3>
    <input type="number" id="peopleCount" placeholder="예: 2"><br><br>

    <h3>준비 시간 (분)</h3>
    <input type="number" id="prepTime" placeholder="예: 30"><br><br>

    <h3>조리 시간 (분)</h3>
    <input type="number" id="cookTime" placeholder="예: 45"><br><br>

    <h3>조리 단계</h3>
    <div id="steps"></div>
    <button type="button" onclick="addStep()">+ 단계 추가</button><br><br>

    <button type="button" onclick="submitRecipe()">레시피 저장</button>
</form>


<script>
    let stepCount = 0;

    function addStep() {
        stepCount++;
        const container = document.getElementById("steps");

        const stepDiv = document.createElement("div");
        stepDiv.className = "step";
        stepDiv.innerHTML = `
            <h4>Step ${stepCount}</h4>
            <textarea placeholder="내용 입력" id="stepContent${stepCount}"></textarea><br>
            <input type="file" id="stepImage${stepCount}"><br><br>
        `;
        container.appendChild(stepDiv);
    }

    async function submitRecipe() {
        const fileNames = [];

        const userId = "u001"; // 예시: 세션에서 가져올 수 있음
        const now = Date.now(); // 밀리초 단위로 유니크 값 생성

        // 메인 이미지
        const mainFile = document.getElementById("mainImage").files[0];
        if (mainFile) {
            fileNames.push(`users/${userId}/recipes/${now}/main/${mainFile.name}`);
        }

        // step 이미지
        for (let i = 1; i <= stepCount; i++) {
            const stepFile = document.getElementById(`stepImage${i}`).files[0];
            if (stepFile) {
                fileNames.push(
                    `users/${userId}/recipes/${now}/steps/${String(i).padStart(2,'0')}_${stepFile.name}`
                );
            }
        }

        // Presigned URL 요청
        const presignResp = await fetch("/recipe/s3/postrecipe", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({fileNames})
        });
        const presignData = await presignResp.json();

        // S3에 PUT
        for (let i = 0; i < presignData.urls.length; i++) {
            const fileObj = presignData.urls[i];
            let file;
            if (i === 0) file = mainFile;
            else file = document.getElementById(`stepImage${i}`).files[0];

            if (file) {
                await fetch(fileObj.uploadUrl, {method: "PUT", body: file});
            }
        }

        // DB에 레시피 등록 요청
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

        await fetch("/recipe/register", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(recipeData)
        });

        alert("레시피 등록 완료!");
    }


</script>
</body>
</html>
