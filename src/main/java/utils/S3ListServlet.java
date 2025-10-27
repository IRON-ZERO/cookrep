package utils;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.Bucket;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/s3list")
public class S3ListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        if (accessKey == null || secretKey == null) {
            out.println("<h3>환경변수가 설정되지 않았습니다.</h3>");
            return;
        }

        try {
            AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
            S3Client s3 = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .region(Region.of("ap-northeast-2"))
                    .build();

            ListBucketsResponse bucketsResponse = s3.listBuckets();
            out.println("<h3>S3 Bucket List:</h3>");
            out.println("<ul>");
            for (Bucket bucket : bucketsResponse.buckets()) {
                out.println("<li>" + bucket.name() + "</li>");
            }
            out.println("</ul>");

        } catch (Exception e) {
            out.println("<h3>오류 발생: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }
}
