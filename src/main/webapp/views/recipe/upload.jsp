<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Recipe Upload</title>
</head>
<body>
<h2>레시피 이미지 업로드</h2>
<form id="uploadForm">
    <input type="file" id="fileInput" name="file"/>
    <button type="button" onclick="uploadFile()">업로드</button>
</form>

<script>
    async function uploadFile() {
        const fileInput = document.getElementById("fileInput");
        if (!fileInput.files.length) {
            alert("파일을 선택해주세요");
            return;
        }

        const file = fileInput.files[0];

        // 서버에서 Presigned URL 요청
        const response = await fetch("/recipe/s3/postrecipe?fileName=" + encodeURIComponent(file.name), {
            method: "POST"
        });
        const data = await response.json();

        if (data.error) {
            alert("URL 생성 실패: " + data.error);
            return;
        }

        const presignedUrl = data.url;

        //  S3에 직접 PUT
        const putResponse = await fetch(presignedUrl, {
            method: "PUT",
            body: file
        });

        if (putResponse.ok) {
            alert("업로드 성공!");
        } else {
            alert("업로드 실패");
        }
    }
</script>
</body>
</html>
