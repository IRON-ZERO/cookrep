package dto.recipe;

public class RecipeSearchDTO {
	private String recipe_id; // 20251001_001
	private String title; // 레시피 제목
	private String thumbnail_image_url; // 썸네일 S3 URL
	private String cookLevel;
	private int views; // 뷰 수
	private int people_count;
	private int prep_time; // 준비시
	private int cook_time; // 요리시간
	private int like; // 좋아요
	private int kcal; // 칼로리

	public RecipeSearchDTO(String recipe_id, String title, String thumbnail_image_url, int views, int people_count,
		int prep_time, int cook_time, int like, int kcal) {
		super();
		this.recipe_id = recipe_id;
		this.title = title;
		this.thumbnail_image_url = thumbnail_image_url;
		this.views = views;
		this.people_count = people_count;
		this.prep_time = prep_time;
		this.cook_time = cook_time;
		this.like = like;
		this.kcal = kcal;
	}

	public String getRecipe_id() {
		return recipe_id;
	}

	public void setRecipe_id(String recipe_id) {
		this.recipe_id = recipe_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail_image_url() {
		return thumbnail_image_url;
	}

	public void setThumbnail_image_url(String thumbnail_image_url) {
		this.thumbnail_image_url = thumbnail_image_url;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getPeople_count() {
		return people_count;
	}

	public void setPeople_count(int people_count) {
		this.people_count = people_count;
	}

	public int getCook_time() {
		return cook_time;
	}

	public void setCook_time(int cook_time) {
		this.cook_time = cook_time;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public int getKcal() {
		return kcal;
	}

	public void setKcal(int kcal) {
		this.kcal = kcal;
	}

	public int getPrep_time() {
		return prep_time;
	}

	public void setPrep_time(int prep_time) {
		this.prep_time = prep_time;
	}

	public String getCookLevel() {
		int pTime = getPrep_time();
		int cTime = getCook_time();
		String easy = "EASY", normal = "NORMAL", hard = "HARD";
		boolean easyCoast = (pTime < 40 && cTime < 40);
		boolean hardCoast = (pTime > 50 && cTime > 60) || cTime > 100;
		if (easyCoast) {
			return easy;
		} else if (hardCoast) {
			return hard;
		}
		return normal;
	}

}
