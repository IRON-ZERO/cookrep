<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.recipe.RecipeDTO" %>
<%@ page import="dto.recipe.RecipeDTO.Step" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Recipe Edit</title>
    <link rel="stylesheet" type="text/css" href="/assets/css/recipe/recipeEdit.css">
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
<main>
    <div class="cont recipe-edit__cont">
        <h2>레시피 수정</h2>
        <form id="editRecipeForm">
            <input type="hidden" name="recipe_id" value="<%= recipe.getRecipe_id() %>">

            <div class="recipe-edit-form-section">
                <label for="recipeTitle" >레시피 제목</label>
                <input class="recipe-edit-input" type="text" name="title" value="<%= recipe.getTitle() %>">
            </div>

            <div>
                <label class="recipe-edit-label">대표 이미지 (필수)</label>
                <img class="recipe-edit-img" src="<%= recipe.getThumbnail_image_url() %>" alt="대표 이미지">
                <input class="recipe-edit-input" type="file" name="thumbnail">
            </div>

            <div>
            <h3>조리 단계</h3>
            <div id="stepContainer">
                            <%
                                List<Step> steps = recipe.getSteps();
                                if (steps != null) {
                                    for (Step step : steps) {
                            %>
                            <div class="recipe-edit-step">
                                <h3>Step <%= step.getStepOrder() %></h3>
                                <textarea class="recipe-edit-textarea" name="step_content_<%= step.getStepOrder() %>"><%= step.getContents() %></textarea>
                                <% if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) { %>
                                <img class="recipe-edit-img" src="<%= step.getImageUrl() %>" alt="Step Image">
                                <% } %>
                                <input class="recipe-edit-input" type="file" name="step_image_<%= step.getStepOrder() %>">
                                <span class="recipe-edit-delete-btn">삭제</span>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>
            <button type="button" id="addStepBtn" class="recipe-edit-button recipe-edit-btn-secondary">단계 추가</button>
            </div>
            <button type="button" id="editRecipeBtn" class="recipe-edit-button recipe-edit-submit-btn">레시피 수정</button>
        </form>
    </div>
</main>

<jsp:include page="/views/components/footerComp.jsp" />

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
        const stepCount = stepContainer.querySelectorAll(".recipe-edit-step").length + 1;

        const newStep = document.createElement("div");
        newStep.className = "recipe-edit-step";
        newStep.innerHTML = `
            <h3>Step ${stepCount}</h3>
            <textarea class="recipe-edit-textarea" name="step_content_${stepCount}" placeholder="조리 단계 설명"></textarea>
            <input class="recipe-edit-input" type="file" name="step_image_${stepCount}">
            <span class="recipe-edit-delete-btn">삭제</span>
        `;

        newStep.querySelector(".recipe-edit-delete-btn").addEventListener("click", () => {
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

            // presigned URL 요청
            const presignResp = await fetch("/recipe/s3/postrecipe", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ fileNames })
            });
            const presignData = await presignResp.json();

            // S3 업로드
            if (mainFile && mainFile.size > 0) {
                const mainUrlObj = presignData.urls.find(u => u.fileName.includes("main"));
                if (mainUrlObj) await fetch(mainUrlObj.uploadUrl, { method: "PUT", body: mainFile });
            }

            for (let sf of stepFiles) {
                const urlObj = presignData.urls.find(u => u.fileName === sf.path);
                if (urlObj) await fetch(urlObj.uploadUrl, { method: "PUT", body: sf.file });
            }

            // DB 업데이트 데이터
            const stepsData = [];
            const stepElements = document.querySelectorAll(".recipe-edit-step");

            stepElements.forEach((stepEl, idx) => {
                const content = stepEl.querySelector("textarea").value;
                const fileInput = stepEl.querySelector("input[type='file']");
                let imageUrl = null;

                if (fileInput && fileInput.files.length > 0) {
                    const stepNum = String(idx + 1).padStart(2, "0");
                    imageUrl = `users/${userId}/recipes/${now}/steps/${stepNum}_${fileInput.files[0].name}`;
                } else if (stepEl.querySelector("img")) {
                    imageUrl = stepEl.querySelector("img").src;
                }

                stepsData.push({ content, imageUrl });
            });

            let thumbnail_url = null;
            if (mainFile && mainFile.size > 0) {
                thumbnail_url = `users/${userId}/recipes/${now}/main/${mainFile.name}`;
            } else {
                thumbnail_url = form.querySelector(".recipe-edit-img")?.src || null;
            }

            const updateData = {
                recipe_id: recipeId,
                title: formData.get("title"),
                thumbnail_url,
                steps: stepsData
            };

            console.log("최종 updateData:", updateData);

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

    // 이벤트 연결
    document.getElementById("addStepBtn").addEventListener("click", addStep);
    document.getElementById("editRecipeBtn").addEventListener("click", submitEditRecipe);

    // 기존 단계 삭제 버튼 이벤트
    document.querySelectorAll(".recipe-edit-delete-btn").forEach(btn => {
        btn.addEventListener("click", (e) => {
            deleteStep(e.target.closest(".recipe-edit-step"));
        });
    });
</script>

<% } %>
<script src="/assets/js/header.js"></script>
</body>
</html>
