package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.recipe.RecipeApiDTO;

public class RecipeAPIService {
	Logger log = Logger.getLogger(RecipeAPIService.class.getName());

	private enum REQUEST_ARGUMENTS {
		MENU, INGREDIENTS
	}

	private RecipeAPIService() {}

	private static class Holder {
		private static final RecipeAPIService INSTANCE = new RecipeAPIService();
	}

	public static RecipeAPIService getInstance() {
		return Holder.INSTANCE;
	}

	public List<RecipeApiDTO> getPublicAPIRecipeList(String s_index, String l_index) {
		List<RecipeApiDTO> result = Collections.emptyList();
		HttpURLConnection conn = null;
		try {
			String urlFormat = apiUrl(s_index, l_index);
			URL url = new URL(urlFormat);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = conn.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
				try (BufferedReader br = new BufferedReader(inputStreamReader);) {
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}

					ObjectMapper mapper = new ObjectMapper();
					JsonNode root = mapper.readTree(sb.toString()).get("COOKRCP01");
					JsonNode data = root.get("row");
					result = mapper.readValue(data.toString(),
						new TypeReference<List<RecipeApiDTO>>() {});
				}
				result.sort((x, y) -> Integer.parseInt(y.getRcpSeo()) - Integer.parseInt(x.getRcpSeo()));
				return result;
			}
		} catch (MalformedURLException e) {
			log.severe("MalformedURLException : " + e);
		} catch (IOException e) {
			log.severe("IOException : " + e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}

	public List<RecipeApiDTO> getPublicAPIRecipeListByName() {
		return null;
	}

	public List<RecipeApiDTO> getPublicAPIRecipeListByIngredients() {
		return null;
	}

	private String apiUrl(String s_index, String l_index) {
		final String API_KEY = "3303680290354193a8e7"; // 예진
		//		final String API_KEY = "06bda7d8439442a5b24f"; // 준혁
		final String URL = "http://openapi.foodsafetykorea.go.kr/api/";
		String formattedString = String.format("/COOKRCP01/json/%s/%s", s_index, l_index);
		String API_URL = URL + API_KEY + formattedString;
		return API_URL;
	}

	private String apiUrl(String s_index, String l_index, String arguments, REQUEST_ARGUMENTS searchType) {
		final String API_KEY = "06bda7d8439442a5b24f";
		final String URL = "http://openapi.foodsafetykorea.go.kr/api/";
		if (searchType == REQUEST_ARGUMENTS.MENU) {
			String formattedString = String.format("/COOKRCP01/json/%s/%s/RCP_NM=%s", s_index, l_index, arguments);
			String API_URL = URL + API_KEY + formattedString;
			return API_URL;
		} else if (searchType == REQUEST_ARGUMENTS.INGREDIENTS) {
			String formattedString = String.format("/COOKRCP01/json/%s/%s/RCP_PARTS_DTLS=%s", s_index, l_index, arguments);
			String API_URL = URL + API_KEY + formattedString;
			return API_URL;
		}
		return apiUrl(s_index, l_index);
	}

}
