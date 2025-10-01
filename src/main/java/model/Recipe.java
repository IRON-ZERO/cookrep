package model;

public class Recipe {
   private int recipe_id;
   private String created_at;
   private String updated_at;

   private User author;

   private String title; //레시피 제목
    private String description; //레시피 전체 설명
   private String foodStyle;
   private String level;
   private int makingTime;

   //단계별 레시피
   private String stepName; //레시피 단계별 제목
   private String imageURL; //레시피 단계별 이미지
   private String contents; //레시피 단계별 설명

    private String hasgTag;
    private int views;
    private int like;

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoodStyle() {
        return foodStyle;
    }

    public void setFoodStyle(String foodStyle) {
        this.foodStyle = foodStyle;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getMakingTime() {
        return makingTime;
    }

    public void setMakingTime(int makingTime) {
        this.makingTime = makingTime;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getHasgTag() {
        return hasgTag;
    }

    public void setHasgTag(String hasgTag) {
        this.hasgTag = hasgTag;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
