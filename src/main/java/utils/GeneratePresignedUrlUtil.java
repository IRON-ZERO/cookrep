package utils;

import java.time.Duration;
import java.util.logging.Logger;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

public class GeneratePresignedUrlUtil {
	public static String generatePresignedUrl(S3Presigner presigner, String fileName) {
		Logger log = Logger.getLogger(GeneratePresignedUrlUtil.class.getName());
		String BUCKET_NAME = "cookrepbucket";
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
			log.severe("GeneratePresignedUrlUtil에서 오류 발생 : " + e);
			return fileName; // 실패 시 원래 Key 반환
		}
	}
}
