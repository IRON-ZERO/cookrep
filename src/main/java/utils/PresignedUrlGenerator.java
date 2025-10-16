package utils;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

public class PresignedUrlGenerator {

    private static final String BUCKET_NAME = "cookrepbucket";
    private static final Region REGION = Region.AP_NORTHEAST_2;
    private final S3Presigner presigner;

    // --- 싱글턴 인스턴스 ---
    private static final PresignedUrlGenerator INSTANCE = new PresignedUrlGenerator();

    // --- private 생성자 ---
    private PresignedUrlGenerator() {
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.presigner = S3Presigner.builder()
                                    .region(REGION)
                                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                                    .build();
    }

    // --- 싱글턴 접근자 ---
    public static PresignedUrlGenerator getInstance() {
        return INSTANCE;
    }

    public String generatePresignedUrl(String fileName) {
        try {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                                                          .bucket(BUCKET_NAME)
                                                          .key(fileName)
                                                          .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                                                            .getObjectRequest(getRequest)
                                                                            .signatureDuration(Duration.ofMinutes(10))
                                                                            .build();

            return presigner.presignGetObject(presignRequest).url().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return fileName;
        }
    }
}
