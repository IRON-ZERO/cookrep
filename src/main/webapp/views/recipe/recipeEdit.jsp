<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.recipe.RecipeDTO" %>
<%@ page import="dto.recipe.RecipeDTO.Step" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>레시피 수정</title>
    <style>
        .recipe-container { max-width: 800px; margin: auto; }
        .step { margin-bottom: 20px; border: 1px solid #ccc; padding: 10px; border-radius: 8px; }
        .step img { max-width: 300px; display: block; margin-top: 10px; }
        .delete-btn { color: red; cursor: pointer; margin-left: 10px; }
        .btn { padding: 10px 20px; margin: 5px; cursor: pointer; }
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
    <h1>레시피 수정</h1>
    <form id="editRecipeForm">
        <!-- hidden id -->
        <input type="hidden" name="recipe_id" value="<%= recipe.getRecipe_id() %>">

        <label>제목</label><br>
        <input type="text" name="title" value="<%= recipe.getTitle() %>"><br><br>

        <label>대표 이미지</label><br>
        <img src="<%= recipe.getThumbnail_image_url() %>" alt="대표 이미지" style="max-width:200px;"><br>
        <input type="file" name="thumbnail"><br><br>

        <h2>조리 단계</h2>
        <div id="stepContainer">
            <%
                List<Step> steps = recipe.getSteps();
                if (steps != null) {
                    for (Step step : steps) {
            %>
            <div class="step">
                <h3>Step <%= step.getStepOrder() %></h3>
                <textarea name="step_content_<%= step.getStepOrder() %>"><%= step.getContents() %></textarea><br>
                <% if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) { %>
                    <img src="<%= step.getImageUrl() %>" alt="Step Image">
                <% } %>
                <input type="file" name="step_image_<%= step.getStepOrder() %>">
                <span class="delete-btn">삭제</span>
            </div>
            <%
                    }
                }
            %>
        </div>

        <button type="button" id="addStepBtn" class="btn">단계 추가</button>
        <button type="button" id="editRecipeBtn" class="btn">레시피 수정</button>
    </form>
</div>

<script type="module">

    // JSP 세션에서 userId 가져오기
    const userId = "<%= (String)session.getAttribute("userId") %>";
    if (!userId || userId === "null") {
        alert("로그인이 필요합니다.");
        window.location.href = "/login";
    }

    // 단계 삭제
    function deleteStep(stepElement) {
        stepElement.remove();
    }

    // 단계 추가
    function addStep() {
        const stepContainer = document.getElementById("stepContainer");
        const stepCount = stepContainer.querySelectorAll(".step").length + 1;

        const newStep = document.createElement("div");
        newStep.className = "step";
        newStep.innerHTML = `
            <h3>Step ${stepCount}</h3>
            <textarea name="step_content_${stepCount}" placeholder="조리 단계 설명"></textarea><br>
            <input type="file" name="step_image_${stepCount}">
            <span class="delete-btn">삭제</span>
        `;

        newStep.querySelector(".delete-btn").addEventListener("click", () => {
            deleteStep(newStep);
        });

        stepContainer.appendChild(newStep);
    }

    // 레시피 수정
    async function submitEditRecipe() {
        try {
            const form = document.getElementById("editRecipeForm");
            const formData = new FormData(form);
            const recipeId = formData.get("recipe_id");
            const now = Date.now();
            // const userId = document.getElementById("userId").value; // hidden input 등에서 가져오기
            // const userId = "u001";
            // const now = new Date().toISOString().replace(/[:.-]/g, ""); // 예: 20251004T2130

            // -----------------------------
            // 1) 업로드할 파일 준비
            // -----------------------------
            const fileNames = [];
            const stepFiles = [];

            const mainFile = formData.get("thumbnail");
            if (mainFile && mainFile.size > 0) {
                const mainPath = `users/${userId}/recipes/${now}/main/${mainFile.name}`;
                fileNames.push(mainPath);
                console.log("대표 이미지 새 URL:", mainPath);
            }

            // step 이미지
            formData.forEach((value, key) => {
                if (value instanceof File && key.startsWith("step_image_") && value.size > 0) {
                    const stepNum = key.replace("step_image_", "").padStart(2, "0");
                    const path = `users/${userId}/recipes/${now}/steps/${stepNum}_${value.name}`;
                    fileNames.push(path);
                    stepFiles.push({ key, path, file: value });
                    console.log(`Step ${stepNum} 이미지 새 URL:`, path);
                }
            });

            // -----------------------------
            // 2) presigned URL 요청
            // -----------------------------
            const presignResp = await fetch("/recipe/s3/postrecipe", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ fileNames })
            });
            const presignData = await presignResp.json();

            // -----------------------------
            // 3) S3 업로드
            // -----------------------------
            if (mainFile && mainFile.size > 0) {
                const mainUrlObj = presignData.urls.find(u => u.fileName.includes("main"));
                if (mainUrlObj) await fetch(mainUrlObj.uploadUrl, { method: "PUT", body: mainFile });
            }

            for (let sf of stepFiles) {
                const urlObj = presignData.urls.find(u => u.fileName === sf.path);
                if (urlObj) await fetch(urlObj.uploadUrl, { method: "PUT", body: sf.file });
            }

            // -----------------------------
            // 4) DB 업데이트용 데이터 생성
            // -----------------------------
            const stepsData = [];
            const stepContainer = document.getElementById("stepContainer");
            const stepElements = stepContainer.querySelectorAll(".step");

            stepElements.forEach((stepEl, idx) => {
                const content = stepEl.querySelector("textarea").value;
                const fileInput = stepEl.querySelector("input[type='file']");
                let imageUrl = null;

                if (fileInput && fileInput.files.length > 0) {
                    const stepNum = String(idx + 1).padStart(2, "0");
                    imageUrl = `users/${userId}/recipes/${now}/steps/${stepNum}_${fileInput.files[0].name}`;
                } else if (stepEl.querySelector("img")) {
                    // 기존 이미지 유지
                    imageUrl = stepEl.querySelector("img").src;
                }

                stepsData.push({ content, imageUrl });
            });

            let thumbnail_url = null;
            if (mainFile && mainFile.size > 0) {
                thumbnail_url = `users/${userId}/recipes/${now}/main/${mainFile.name}`;
            } else {
                thumbnail_url = form.querySelector("img")?.src || null;
            }

            const updateData = {
                recipe_id: recipeId,
                title: formData.get("title"),
                thumbnail_url,  // 서버 DB 컬럼명과 일치
                steps: stepsData
            };

            console.log("최종 updateData:", updateData);

            // -----------------------------
            // 5) DB 업데이트 요청
            // -----------------------------
            await fetch("/recipe/update", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(updateData)
            });

            alert("레시피 수정 완료!");
            location.href = `/mypage/recipe?action=detail&recipe_id=${recipeId}`;

        } catch (err) {
            console.error("레시피 수정 실패", err);
            alert("레시피 수정 중 오류가 발생했습니다.");
        }
    }




    // 이벤트 리스너 연결
    document.getElementById("addStepBtn").addEventListener("click", addStep);
    document.getElementById("editRecipeBtn").addEventListener("click", submitEditRecipe);

    // 기존 단계의 삭제 버튼에도 이벤트 리스너 연결
    document.querySelectorAll(".step .delete-btn").forEach(btn => {
        btn.addEventListener("click", (e) => {
            deleteStep(e.target.closest(".step"));
        });
    });
</script>

<% } %>
</body>
</html>
