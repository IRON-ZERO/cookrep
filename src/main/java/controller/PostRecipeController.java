package controller;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

@WebServlet("/recipe/s3/postrecipe")
public class PostRecipeController extends HttpServlet {

    private S3Presigner presigner;
    private final String BUCKET_NAME = "cookrepbucket";

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

        String fileName = request.getParameter("fileName");
        if (fileName == null || fileName.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("{\"error\":\"fileName required\"}");
            return;
        }

        try {
            // S3 업로드용 Presigned URL 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .putObjectRequest(putObjectRequest)
                    .signatureDuration(Duration.ofMinutes(10))
                    .build();

            String presignedUrl = presigner.presignPutObject(presignRequest).url().toString();

            // JSON 반환
            response.getWriter().write("{\"url\":\"" + presignedUrl + "\"}");
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            e.printStackTrace(response.getWriter());
        }
    }
}

