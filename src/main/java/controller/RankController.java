package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.recipe.RecipeSearchDTO;
import service.SearchService;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import utils.GeneratePresignedUrlUtil;

@WebServlet("/rank")
public class RankController extends HttpServlet {

	SearchService searchService = SearchService.getInstance();
	private S3Presigner presigner;

	@Override
	public void init() {
		String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
		String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

		AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

		presigner = S3Presigner.builder()
			.credentialsProvider(StaticCredentialsProvider.create(credentials))
			.region(Region.of("ap-northeast-2")).build();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<RecipeSearchDTO> rank = searchService.searchRecipeForRank();
		rank.forEach(x -> x
			.setThumbnail_image_url(GeneratePresignedUrlUtil.generatePresignedUrl(presigner, x.getThumbnail_image_url())));
		req.setAttribute("ranking", rank);
		req.getRequestDispatcher("/views/rank/rank.jsp").forward(req, resp);
	}

	@Override
	public void destroy() {
		presigner.close();

	}
}
