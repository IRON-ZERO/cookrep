package service;

import dto.recipe.RecipeDTO;
import repository.RecipeDAO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecipeService {

//    private RecipeDAO dao = new RecipeDAO();
//
//    // recipe_id 자동생성
//    public String generateRecipeId(){
//        String today = new SimpleDateFormat("yyyyMMDD").format(new Date());
//        String lastId = dao.getLastRecipeId(today);
//
//        int nextSeq =1;
//        if(lastId != null){
//            String seqStr = lastId.substring(9); // 20251002_001 -> 001
//            nextSeq = Integer.parseInt(seqStr) + 1;
//        }
//
//        return today + "_"+ String.format("%03d", nextSeq);
//
//    }
//
//    //DTO의 recipe_id 추가
//    public void registerRecipe(RecipeDTO recipe){
//        String newId = generateRecipeId();
//        recipe.setRecipe_id(newId);
//        dao.insertRecipe(recipe);
//    }
}
