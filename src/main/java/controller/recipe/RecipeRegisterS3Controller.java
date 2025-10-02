package controller.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

// post시 서명된 url 반환해주는 컨트롤러
@WebServlet("/recipe/s3/postrecipe")
public class RecipeRegisterS3Controller extends HttpServlet {

    private S3Presigner presigner;
    private final String BUCKET_NAME = "cookrepbucket";
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void init() {
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");


        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of("ap-northeast-2"))
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("utf-8");

        try {
            // 요청 JSON 읽기
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            Map<String, Object> reqMap = objectMapper.readValue(sb.toString(), Map.class);

            // fileNames 배열 꺼내오기
            List<String> fileNames = (List<String>) reqMap.get("fileNames");
            if (fileNames == null || fileNames.isEmpty()) {
                response.setStatus(400);
                response.getWriter().write("{\"error\":\"fileNames required\"}");
                return;
            }

            // response JSON 구조 만들기
            ObjectNode resultJson = objectMapper.createObjectNode();
            ArrayNode urlsArray = objectMapper.createArrayNode();

            for (String fileName : fileNames) {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(fileName)
                        .build();

                PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                        .putObjectRequest(putObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10))
                        .build();

                String presignedUrl = presigner.presignPutObject(presignRequest).url().toString();

                ObjectNode fileObj = objectMapper.createObjectNode();
                fileObj.put("fileName", fileName);
                fileObj.put("uploadUrl", presignedUrl);

                urlsArray.add(fileObj);
            }

            resultJson.set("urls", urlsArray);

            response.getWriter().write(objectMapper.writeValueAsString(resultJson));

        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        // 조회용 Presigned URL
//        resp.setContentType("application/json;charset=UTF-8");
//        try {
//            String fileName = req.getParameter("fileName");
//            if (fileName == null || fileName.isEmpty()) {
//                resp.setStatus(400);
//                resp.getWriter().write("{\"error\":\"fileName required\"}");
//                return;
//            }
//
//            GetObjectRequest getRequest = GetObjectRequest.builder()
//                    .bucket(BUCKET_NAME)
//                    .key(fileName)
//                    .build();
//
//            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                    .getObjectRequest(getRequest)
//                    .signatureDuration(Duration.ofMinutes(10))
//                    .build();
//
//            String presignedUrl = presigner.presignGetObject(presignRequest).url().toString();
//            ObjectNode resultJson = objectMapper.createObjectNode();
//            resultJson.put("fileName", fileName);
//            resultJson.put("downloadUrl", presignedUrl);
//
//            resp.getWriter().write(objectMapper.writeValueAsString(resultJson));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            resp.setStatus(500);
//            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
//        }
//    }

    }

