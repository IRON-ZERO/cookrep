use cookrep;
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 기존 테이블 삭제 (참조 순서 고려)
DROP TABLE IF EXISTS Scrap;
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS RecipeIngredient;
DROP TABLE IF EXISTS RecipeSteps;
DROP TABLE IF EXISTS Recipe;
DROP TABLE IF EXISTS userIngredient;
DROP TABLE IF EXISTS Ingredient;
DROP TABLE IF EXISTS User;

-- 사용자 테이블
CREATE TABLE User (
                      user_id VARCHAR(36) PRIMARY KEY,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      nickname VARCHAR(20) NOT NULL UNIQUE,
                      first_name VARCHAR(20) NOT NULL,
                      last_name VARCHAR(20) NOT NULL,
                      country VARCHAR(50),
                      city  VARCHAR(50),
                      email VARCHAR(100) NOT NULL UNIQUE,
                      password VARCHAR(90)

);

-- 재료 테이블
CREATE TABLE Ingredient (
                            ingredient_id INT AUTO_INCREMENT PRIMARY KEY,   -- INT로 통일
                            name VARCHAR(50) NOT NULL unique ,                       -- 이름 길이 여유 있게
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 사용자-재료 관계 (사용자 냉장고)
CREATE TABLE userIngredient (
                                user_id VARCHAR(36) NOT NULL,                   -- users.user_id와 타입 맞춤
                                ingredient_id INT NOT NULL,                      -- ingredient_id와 타입 맞춤
                                PRIMARY KEY (user_id, ingredient_id),
                                FOREIGN KEY (user_id) REFERENCES User(user_id) on delete cascade,
                                FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id) on delete cascade
);

-- 레시피 테이블
CREATE TABLE Recipe (
                        recipe_id VARCHAR(50) PRIMARY KEY,  -- 예: 20251001_001
                        user_id VARCHAR(36),
                        title VARCHAR(100),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        thumbnail_image_url VARCHAR(500),  -- S3 업로드 경로 (썸네일용 완성 이미지)
                        views INT DEFAULT 0,
                        people_count INT DEFAULT 0,
                        prep_time INT DEFAULT 0,
                        cook_time INT DEFAULT 0,
                        `like` INT DEFAULT 0,
                        kcal INT,
                        FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- 레시피 단계 테이블
CREATE TABLE RecipeSteps (
                             step_id INT AUTO_INCREMENT PRIMARY KEY,
                             recipe_id VARCHAR(50) NOT NULL,
                             step_order INT NOT NULL,  -- 단계 순서
                             contents TEXT,    -- 긴 글 저장
                             image_url VARCHAR(500),  -- S3 업로드 경로
                             FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id)
);

-- 레시피-재료 테이블 (사용 재료)
CREATE TABLE RecipeIngredient (
                                  recipe_id VARCHAR(50),
                                  ingredient_id INT,
                                  count VARCHAR(20), -- 사용한 수량(단위랑 합쳐서 문자열)
                                  PRIMARY KEY (recipe_id, ingredient_id),
                                  FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id) on delete cascade,
                                  FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id) on delete cascade
);

-- 댓글 테이블
CREATE TABLE Comment (
                         comment_id INT AUTO_INCREMENT PRIMARY KEY,   -- 댓글 수 많지 않으면 INT 충분
                         user_id VARCHAR(36) NOT NULL,
                         recipe_id VARCHAR(50) NOT NULL,
                         contents TEXT NOT NULL,                      -- 긴 글은 TEXT
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES User(user_id),
                         FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id)
);

-- 스크랩 테이블
CREATE TABLE Scrap (
                       recipe_id VARCHAR(50) NOT NULL,
                       user_id VARCHAR(36) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       PRIMARY KEY (recipe_id, user_id),
                       FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id) on delete cascade,
                       FOREIGN KEY (user_id) REFERENCES User(user_id) on delete cascade
);

-- 레시피 더미데이터
/*
-- Query: select * from Recipe
LIMIT 0, 1000

-- Date: 2025-10-15 00:06
*/
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('083b9641-822c-4c77-b441-490025d93cb1','7024dc66-94c8-4495-9b4b-160d0e764be5','여기가 이테리다! 크림 리조또','2025-10-14 12:37:04','2025-10-14 14:45:52','https://images.unsplash.com/photo-1682428617976-f25633ed8469?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1206',825,2,49,59,174,384);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('0927ba06-bc92-4345-85e4-34893b845877','7024dc66-94c8-4495-9b4b-160d0e764be5','불고기 덮밥','2025-10-14 12:37:04','2025-10-14 13:11:42','https://images.unsplash.com/photo-1564836235910-c3055ca0f912?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170',238,5,43,8,287,303);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('17123168-5fde-4d8b-8113-e5da0690b22f','7024dc66-94c8-4495-9b4b-160d0e764be5','갈비찜','2025-10-14 12:37:04','2025-10-14 13:11:42','https://cdn.pixabay.com/photo/2017/07/04/19/24/ribs-2472270_1280.jpg',389,1,27,113,176,718);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('1a9a2cd9-7dfb-4f31-8e2f-383822a54de3','7024dc66-94c8-4495-9b4b-160d0e764be5','참치 샐러드','2025-10-14 12:37:04','2025-10-14 13:11:42','https://images.unsplash.com/photo-1604909052743-94e838986d24?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=880',257,5,60,6,58,798);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('1e159282-6d31-496a-a782-e7e21b1e38dd','7024dc66-94c8-4495-9b4b-160d0e764be5','비오는 날에는 감자전','2025-10-14 12:37:04','2025-10-14 14:45:52','https://cdn.pixabay.com/photo/2015/03/31/16/34/kartoffelpuffer-701285_1280.jpg',733,6,49,74,214,325);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('1ee7dd2e-6854-4b53-92af-0ee62e7f8858','7024dc66-94c8-4495-9b4b-160d0e764be5','카레라이스','2025-10-14 12:37:04','2025-10-14 13:31:30','https://images.unsplash.com/photo-1706145779556-f2c642db2699?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1631',959,6,46,14,87,646);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('24d22928-44e2-46d4-8df6-6840673fceeb','7024dc66-94c8-4495-9b4b-160d0e764be5','꾸덕한 치즈케이크를 아라보자','2025-10-14 12:37:04','2025-10-14 14:45:52','https://images.unsplash.com/photo-1635327173758-85badf17f995?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1127',905,6,34,23,135,242);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('30244bff-d79c-4712-a922-7aa11514264c','7024dc66-94c8-4495-9b4b-160d0e764be5','일본 장인도 울고갈 오믈렛','2025-10-14 12:37:04','2025-10-14 14:45:52','https://images.unsplash.com/photo-1743148509702-2198b23ede1c?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170',270,1,51,63,274,227);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('3c1733b5-c807-4c34-838a-a74763534bfe','7024dc66-94c8-4495-9b4b-160d0e764be5','드레곤볼 파스타','2025-10-14 12:37:04','2025-10-14 14:45:52','https://images.unsplash.com/photo-1674456720401-1557c76bf72c?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=2070',228,2,52,18,279,189);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('52187a73-7362-4169-8dfb-4949d0d0c633','7024dc66-94c8-4495-9b4b-160d0e764be5','먹으면 코끼리가 되는 야채 샐러드','2025-10-14 12:37:04','2025-10-14 14:45:52','https://images.unsplash.com/photo-1751124929750-0c4d0d6e3479?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1634',296,1,59,34,51,489);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('600fef1f-6a7e-4fe2-9db3-2b9509811b49','7024dc66-94c8-4495-9b4b-160d0e764be5','불닭볶음면 뺨치는 매운 볶음면','2025-10-14 12:37:04','2025-10-14 13:31:30','https://images.unsplash.com/photo-1683112688202-86eacb160a61?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170',112,2,45,25,216,710);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('6b6aa094-1e8d-4c34-847e-8fa664560635','7024dc66-94c8-4495-9b4b-160d0e764be5','비빔국수','2025-10-14 12:37:04','2025-10-14 13:31:30','https://images.unsplash.com/photo-1579576792017-054fe0e86b79?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=687',379,3,18,90,136,818);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('73940cf2-248f-415a-ac49-1b6c3b34d8a5','7024dc66-94c8-4495-9b4b-160d0e764be5','버터 마늘 새우로 만든 감바스','2025-10-14 12:37:04','2025-10-14 14:45:52','https://images.unsplash.com/photo-1514944288352-fffac99f0bdf?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170',947,6,49,76,112,801);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('85f94d2d-52df-489f-acd4-63053d8a7037','7024dc66-94c8-4495-9b4b-160d0e764be5','케데헌도 먹어보고 싶어하는 그 김밥','2025-10-14 12:37:04','2025-10-14 14:45:52','https://cdn.pixabay.com/photo/2015/06/09/15/12/kim-rice-803637_1280.jpg',323,4,22,13,108,680);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('966d196e-85c2-460a-82fc-def781fab24a','7024dc66-94c8-4495-9b4b-160d0e764be5','연인에게 주려다 내가 다먹은 마카롱','2025-10-14 12:37:04','2025-10-14 13:31:30','https://images.unsplash.com/photo-1699213999502-cdef04208915?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170',746,2,15,64,194,376);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('9a1f1594-4150-477c-91f0-4a50f57b3587','7024dc66-94c8-4495-9b4b-160d0e764be5','연돈안가도 됨','2025-10-14 12:37:04','2025-10-14 13:31:30','https://cdn.pixabay.com/photo/2018/03/06/09/02/food-3202918_1280.jpg',141,5,36,16,24,981);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('9b594ec6-51c1-4b21-89e4-c71e74024eee','7024dc66-94c8-4495-9b4b-160d0e764be5','너네 할매 떡볶이','2025-10-14 12:37:04','2025-10-14 15:00:04','https://cdn.pixabay.com/photo/2016/07/23/08/00/food-1536421_960_720.jpg',284,4,45,111,186,266);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('a94ff2a2-8afb-434d-b33a-319d58ab5e8b','7024dc66-94c8-4495-9b4b-160d0e764be5','냉장고에 있는 채소로 스프를 만들어보자','2025-10-14 12:37:04','2025-10-14 14:45:52','https://images.unsplash.com/photo-1652408414631-f09eae7cc1db?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170',252,6,40,73,134,864);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('afb20daf-53aa-4198-9fe5-6a0bda24e034','7024dc66-94c8-4495-9b4b-160d0e764be5','김.볶.밥','2025-10-14 12:37:04','2025-10-14 14:45:52','https://cdn.pixabay.com/photo/2014/01/09/10/14/kimchi-fried-rice-241051_1280.jpg',654,1,6,99,140,350);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('b8dbee41-5f80-43fb-b2ec-cfbc9a47bd08','7024dc66-94c8-4495-9b4b-160d0e764be5','증조할머니가 알려준 잡채','2025-10-14 12:37:04','2025-10-14 14:45:52','https://cdn.pixabay.com/photo/2020/10/19/01/32/chop-suey-5666461_1280.jpg',643,5,60,51,295,296);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('bdc93574-8ad5-407d-9b3d-f0326e4676c3','7024dc66-94c8-4495-9b4b-160d0e764be5','지능이 떨어지는 차돌 소고기 된장찌개','2025-10-14 12:37:04','2025-10-14 14:45:52','https://cdn.pixabay.com/photo/2019/11/23/12/51/meat-4647002_1280.jpg',459,5,22,108,3,877);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('ce66098d-24c4-4502-8b34-49b0343cbeb6','7024dc66-94c8-4495-9b4b-160d0e764be5','5대째 내려오는 오징어볶음','2025-10-14 12:37:04','2025-10-14 15:00:04','https://cdn.pixabay.com/photo/2019/09/05/01/08/food-4452839_1280.jpg',906,5,53,39,174,214);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('d16d18b5-0849-4bf7-8a2e-77f2ff556326','7024dc66-94c8-4495-9b4b-160d0e764be5','이게 피자야 뭐야? 라자냐~','2025-10-14 12:37:04','2025-10-14 15:00:04','https://images.unsplash.com/photo-1709429790175-b02bb1b19207?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1632',598,4,42,56,185,324);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('d810bb9a-055e-4942-b10f-0d8d993a185d','7024dc66-94c8-4495-9b4b-160d0e764be5','노르웨이에서 갓 잡은 연어로 만든 스테끼~','2025-10-14 12:37:04','2025-10-14 15:00:04','https://images.unsplash.com/photo-1656389863625-59de2275fb7e?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170',996,4,10,75,150,949);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('e30cdd40-e964-409a-967c-89e7ce560eee','7024dc66-94c8-4495-9b4b-160d0e764be5','중년 떡볶이','2025-10-14 12:37:04','2025-10-14 15:00:04','https://cdn.pixabay.com/photo/2018/11/04/09/08/toppokki-3793393_1280.jpg',897,6,25,32,255,505);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('e433ef27-9423-4aeb-8684-a4ba1abd56c3','7024dc66-94c8-4495-9b4b-160d0e764be5','이삭토스트의 대항마! 허니갈릭 프렌치 토스트','2025-10-14 12:37:04','2025-10-14 15:00:04','https://images.unsplash.com/photo-1584776296944-ab6fb57b0bdd?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1158',721,1,7,89,116,891);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('e637ae4c-7669-4030-bf78-38bd784d5770','7024dc66-94c8-4495-9b4b-160d0e764be5','왼손으로 먹어도 용서되는 치킨 커리','2025-10-14 12:37:04','2025-10-14 15:00:04','https://images.unsplash.com/photo-1565557623262-b51c2513a641?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1671',604,4,7,8,47,323);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('f07a9ced-27aa-4a99-834d-a90fb48892af','7024dc66-94c8-4495-9b4b-160d0e764be5','유학 시절 아플때마다 먹은 소고기 스튜','2025-10-14 12:37:04','2025-10-14 15:00:04','https://images.unsplash.com/photo-1664741662725-bd131742b7b7?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170',332,1,19,110,16,924);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('f4706f3b-9000-45d8-8f60-a00ea47db4f1','7024dc66-94c8-4495-9b4b-160d0e764be5','할머니 배불러요..애플파이','2025-10-14 12:37:04','2025-10-14 15:00:04','https://cdn.pixabay.com/photo/2018/10/04/11/37/woman-3723444_1280.jpg',65,4,29,81,239,641);
INSERT INTO Recipe (`recipe_id`,`user_id`,`title`,`created_at`,`updated_at`,`thumbnail_image_url`,`views`,`people_count`,`prep_time`,`cook_time`,`like`,`kcal`) VALUES ('f873c4c4-599b-4f3a-99e7-e5b571219547','7024dc66-94c8-4495-9b4b-160d0e764be5','와인/맥주 안주로 딱인 브루스케타','2025-10-14 12:37:04','2025-10-14 15:00:04','https://images.unsplash.com/photo-1623962470382-a01d602b3a16?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170',159,2,53,48,52,194);


-- 재료 더미
INSERT ignore INTO Ingredient (name) VALUES
                                         ('양파'), ('당근'), ('감자'), ('마늘'), ('대파'),
                                         ('소고기'), ('돼지고기'), ('닭고기'), ('계란'), ('쌀'),
                                         ('밀가루'), ('버터'), ('치즈'), ('우유'), ('생크림'),
                                         ('새우'), ('오징어'), ('김치'), ('간장'), ('고추장'),
                                         ('설탕'), ('소금'), ('후추'), ('식용유'), ('올리브오일'),
                                         ('빵'), ('파스타면'), ('토마토'), ('양배추'), ('양상추'),
                                         ('참치'), ('사과'), ('바나나'), ('초콜릿'), ('고구마'),
                                         ('브로콜리'), ('버섯'), ('파프리카'), ('두부'), ('양송이');

-- 사용자-재료(냉장고) 더미 (user_id 수정해야 함.)
INSERT INTO userIngredient (user_id, ingredient_id) VALUES
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 1),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 2),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 3),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 4),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 5),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 6),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 8),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 9),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 10),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 13),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 17),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 18),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 19),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 20),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 23),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 25),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 26),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 27),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 30),
                                                        ('430a103b-e20a-4937-9445-72dcd9d1116a', 37);

-- 레시피-재료 더미
-- 1. 여기가 이테리다! 크림 리조또
INSERT INTO RecipeIngredient VALUES
                                 ('083b9641-822c-4c77-b441-490025d93cb1', 27, '100g'),  -- 파스타면(리조또용 쌀 대체)
                                 ('083b9641-822c-4c77-b441-490025d93cb1', 1, '1/2개'),
                                 ('083b9641-822c-4c77-b441-490025d93cb1', 13, '30g'),
                                 ('083b9641-822c-4c77-b441-490025d93cb1', 15, '150ml'),
                                 ('083b9641-822c-4c77-b441-490025d93cb1', 22, '약간');

-- 2. 불고기 덮밥
INSERT INTO RecipeIngredient VALUES
                                 ('0927ba06-bc92-4345-85e4-34893b845877', 7, '200g'),
                                 ('0927ba06-bc92-4345-85e4-34893b845877', 1, '1/2개'),
                                 ('0927ba06-bc92-4345-85e4-34893b845877', 19, '2T'),
                                 ('0927ba06-bc92-4345-85e4-34893b845877', 20, '1T'),
                                 ('0927ba06-bc92-4345-85e4-34893b845877', 10, '1공기');

-- 3. 갈비찜
INSERT INTO RecipeIngredient VALUES
                                 ('17123168-5fde-4d8b-8113-e5da0690b22f', 6, '600g'),
                                 ('17123168-5fde-4d8b-8113-e5da0690b22f', 2, '1개'),
                                 ('17123168-5fde-4d8b-8113-e5da0690b22f', 3, '1개'),
                                 ('17123168-5fde-4d8b-8113-e5da0690b22f', 19, '3T'),
                                 ('17123168-5fde-4d8b-8113-e5da0690b22f', 20, '1T');

-- 4. 참치 샐러드
INSERT INTO RecipeIngredient VALUES
                                 ('1a9a2cd9-7dfb-4f31-8e2f-383822a54de3', 31, '1캔'),
                                 ('1a9a2cd9-7dfb-4f31-8e2f-383822a54de3', 30, '3장'),
                                 ('1a9a2cd9-7dfb-4f31-8e2f-383822a54de3', 9, '1개'),
                                 ('1a9a2cd9-7dfb-4f31-8e2f-383822a54de3', 22, '약간');

-- 5. 비오는 날에는 감자전
INSERT INTO RecipeIngredient VALUES
                                 ('1e159282-6d31-496a-a782-e7e21b1e38dd', 3, '2개'),
                                 ('1e159282-6d31-496a-a782-e7e21b1e38dd', 11, '100g'),
                                 ('1e159282-6d31-496a-a782-e7e21b1e38dd', 4, '1쪽'),
                                 ('1e159282-6d31-496a-a782-e7e21b1e38dd', 23, '1T'),
                                 ('1e159282-6d31-496a-a782-e7e21b1e38dd', 22, '약간');

-- 6. 카레라이스
INSERT INTO RecipeIngredient VALUES
                                 ('1ee7dd2e-6854-4b53-92af-0ee62e7f8858', 3, '1개'),
                                 ('1ee7dd2e-6854-4b53-92af-0ee62e7f8858', 2, '1/2개'),
                                 ('1ee7dd2e-6854-4b53-92af-0ee62e7f8858', 19, '2T'),
                                 ('1ee7dd2e-6854-4b53-92af-0ee62e7f8858', 20, '1T'),
                                 ('1ee7dd2e-6854-4b53-92af-0ee62e7f8858', 10, '1공기');

-- 7. 꾸덕한 치즈케이크를 아라보자
INSERT INTO RecipeIngredient VALUES
                                 ('24d22928-44e2-46d4-8df6-6840673fceeb', 13, '200g'),
                                 ('24d22928-44e2-46d4-8df6-6840673fceeb', 14, '100ml'),
                                 ('24d22928-44e2-46d4-8df6-6840673fceeb', 12, '50g'),
                                 ('24d22928-44e2-46d4-8df6-6840673fceeb', 21, '2T'),
                                 ('24d22928-44e2-46d4-8df6-6840673fceeb', 9, '2개');

-- 8. 일본 장인도 울고갈 오믈렛
INSERT INTO RecipeIngredient VALUES
                                 ('30244bff-d79c-4712-a922-7aa11514264c', 9, '3개'),
                                 ('30244bff-d79c-4712-a922-7aa11514264c', 12, '1T'),
                                 ('30244bff-d79c-4712-a922-7aa11514264c', 14, '30ml'),
                                 ('30244bff-d79c-4712-a922-7aa11514264c', 22, '약간');

-- 9. 드레곤볼 파스타
INSERT INTO RecipeIngredient VALUES
                                 ('3c1733b5-c807-4c34-838a-a74763534bfe', 27, '150g'),
                                 ('3c1733b5-c807-4c34-838a-a74763534bfe', 28, '1개'),
                                 ('3c1733b5-c807-4c34-838a-a74763534bfe', 13, '20g'),
                                 ('3c1733b5-c807-4c34-838a-a74763534bfe', 23, '1T');

-- 10. 먹으면 코끼리가 되는 야채 샐러드
INSERT INTO RecipeIngredient VALUES
                                 ('52187a73-7362-4169-8dfb-4949d0d0c633', 30, '3장'),
                                 ('52187a73-7362-4169-8dfb-4949d0d0c633', 38, '1/2개'),
                                 ('52187a73-7362-4169-8dfb-4949d0d0c633', 34, '조각 3개'),
                                 ('52187a73-7362-4169-8dfb-4949d0d0c633', 22, '약간');

-- 11. 불닭볶음면 뺨치는 매운 볶음면
INSERT INTO RecipeIngredient VALUES
                                 ('600fef1f-6a7e-4fe2-9db3-2b9509811b49', 27, '120g'),
                                 ('600fef1f-6a7e-4fe2-9db3-2b9509811b49', 20, '2T'),
                                 ('600fef1f-6a7e-4fe2-9db3-2b9509811b49', 4, '2쪽'),
                                 ('600fef1f-6a7e-4fe2-9db3-2b9509811b49', 22, '약간');

-- 12. 비빔국수
INSERT INTO RecipeIngredient VALUES
                                 ('6b6aa094-1e8d-4c34-847e-8fa664560635', 27, '150g'),
                                 ('6b6aa094-1e8d-4c34-847e-8fa664560635', 20, '2T'),
                                 ('6b6aa094-1e8d-4c34-847e-8fa664560635', 21, '1T'),
                                 ('6b6aa094-1e8d-4c34-847e-8fa664560635', 22, '약간');

-- 13. 버터 마늘 새우로 만든 감바스
INSERT INTO RecipeIngredient VALUES
                                 ('73940cf2-248f-415a-ac49-1b6c3b34d8a5', 16, '10마리'),
                                 ('73940cf2-248f-415a-ac49-1b6c3b34d8a5', 4, '3쪽'),
                                 ('73940cf2-248f-415a-ac49-1b6c3b34d8a5', 12, '20g'),
                                 ('73940cf2-248f-415a-ac49-1b6c3b34d8a5', 25, '3T');

-- 14. 케데헌도 먹어보고 싶어하는 그 김밥
INSERT INTO RecipeIngredient VALUES
                                 ('85f94d2d-52df-489f-acd4-63053d8a7037', 10, '1공기'),
                                 ('85f94d2d-52df-489f-acd4-63053d8a7037', 17, '1장'),
                                 ('85f94d2d-52df-489f-acd4-63053d8a7037', 9, '1개'),
                                 ('85f94d2d-52df-489f-acd4-63053d8a7037', 1, '조금');

-- 15. 연인에게 주려다 내가 다먹은 마카롱
INSERT INTO RecipeIngredient VALUES
                                 ('966d196e-85c2-460a-82fc-def781fab24a', 12, '50g'),
                                 ('966d196e-85c2-460a-82fc-def781fab24a', 21, '1T'),
                                 ('966d196e-85c2-460a-82fc-def781fab24a', 33, '조각 1개'),
                                 ('966d196e-85c2-460a-82fc-def781fab24a', 9, '2개');

-- 16. 연돈안가도 됨 (돈까스)
INSERT INTO RecipeIngredient VALUES
                                 ('9a1f1594-4150-477c-91f0-4a50f57b3587', 7, '150g'),
                                 ('9a1f1594-4150-477c-91f0-4a50f57b3587', 11, '100g'),
                                 ('9a1f1594-4150-477c-91f0-4a50f57b3587', 9, '1개'),
                                 ('9a1f1594-4150-477c-91f0-4a50f57b3587', 23, '1T');

-- 17. 너네 할매 떡볶이
INSERT INTO RecipeIngredient VALUES
                                 ('9b594ec6-51c1-4b21-89e4-c71e74024eee', 20, '3T'),
                                 ('9b594ec6-51c1-4b21-89e4-c71e74024eee', 19, '1T'),
                                 ('9b594ec6-51c1-4b21-89e4-c71e74024eee', 3, '1개'),
                                 ('9b594ec6-51c1-4b21-89e4-c71e74024eee', 22, '약간');

-- 18. 냉장고에 있는 채소로 스프를 만들어보자
INSERT INTO RecipeIngredient VALUES
                                 ('a94ff2a2-8afb-434d-b33a-319d58ab5e8b', 3, '1개'),
                                 ('a94ff2a2-8afb-434d-b33a-319d58ab5e8b', 29, '3장'),
                                 ('a94ff2a2-8afb-434d-b33a-319d58ab5e8b', 37, '2개'),
                                 ('a94ff2a2-8afb-434d-b33a-319d58ab5e8b', 15, '100ml');

-- 19. 김.볶.밥
INSERT INTO RecipeIngredient VALUES
                                 ('afb20daf-53aa-4198-9fe5-6a0bda24e034', 17, '1장'),
                                 ('afb20daf-53aa-4198-9fe5-6a0bda24e034', 10, '1공기'),
                                 ('afb20daf-53aa-4198-9fe5-6a0bda24e034', 4, '2쪽'),
                                 ('afb20daf-53aa-4198-9fe5-6a0bda24e034', 22, '약간');

-- 20. 증조할머니가 알려준 잡채
INSERT INTO RecipeIngredient VALUES
                                 ('b8dbee41-5f80-43fb-b2ec-cfbc9a47bd08', 2, '1개'),
                                 ('b8dbee41-5f80-43fb-b2ec-cfbc9a47bd08', 3, '1개'),
                                 ('b8dbee41-5f80-43fb-b2ec-cfbc9a47bd08', 38, '1개'),
                                 ('b8dbee41-5f80-43fb-b2ec-cfbc9a47bd08', 19, '3T');

-- 21. 지능이 떨어지는 차돌 소고기 된장찌개
INSERT INTO RecipeIngredient VALUES
                                 ('bdc93574-8ad5-407d-9b3d-f0326e4676c3', 6, '150g'),
                                 ('bdc93574-8ad5-407d-9b3d-f0326e4676c3', 1, '1/2개'),
                                 ('bdc93574-8ad5-407d-9b3d-f0326e4676c3', 39, '1/2모'),
                                 ('bdc93574-8ad5-407d-9b3d-f0326e4676c3', 19, '2T');

-- 22. 5대째 내려오는 오징어볶음
INSERT INTO RecipeIngredient VALUES
                                 ('ce66098d-24c4-4502-8b34-49b0343cbeb6', 17, '100g'),
                                 ('ce66098d-24c4-4502-8b34-49b0343cbeb6', 1, '1/2개'),
                                 ('ce66098d-24c4-4502-8b34-49b0343cbeb6', 20, '2T'),
                                 ('ce66098d-24c4-4502-8b34-49b0343cbeb6', 22, '약간');

-- 23. 이게 피자야 뭐야? 라자냐~
INSERT INTO RecipeIngredient VALUES
                                 ('d16d18b5-0849-4bf7-8a2e-77f2ff556326', 11, '150g'),
                                 ('d16d18b5-0849-4bf7-8a2e-77f2ff556326', 13, '100g'),
                                 ('d16d18b5-0849-4bf7-8a2e-77f2ff556326', 28, '1개'),
                                 ('d16d18b5-0849-4bf7-8a2e-77f2ff556326', 14, '50ml');

-- 24. 노르웨이에서 갓 잡은 연어로 만든 스테끼~
INSERT INTO RecipeIngredient VALUES
                                 ('d810bb9a-055e-4942-b10f-0d8d993a185d', 16, '1조각'),
                                 ('d810bb9a-055e-4942-b10f-0d8d993a185d', 25, '1T'),
                                 ('d810bb9a-055e-4942-b10f-0d8d993a185d', 23, '1T'),
                                 ('d810bb9a-055e-4942-b10f-0d8d993a185d', 22, '약간');

-- 25. 중년 떡볶이
INSERT INTO RecipeIngredient VALUES
                                 ('e30cdd40-e964-409a-967c-89e7ce560eee', 20, '2T'),
                                 ('e30cdd40-e964-409a-967c-89e7ce560eee', 19, '1T'),
                                 ('e30cdd40-e964-409a-967c-89e7ce560eee', 2, '1/2개'),
                                 ('e30cdd40-e964-409a-967c-89e7ce560eee', 22, '약간');

-- 26. 이삭토스트의 대항마! 허니갈릭 프렌치 토스트
INSERT INTO RecipeIngredient VALUES
                                 ('e433ef27-9423-4aeb-8684-a4ba1abd56c3', 26, '2장'),
                                 ('e433ef27-9423-4aeb-8684-a4ba1abd56c3', 12, '1T'),
                                 ('e433ef27-9423-4aeb-8684-a4ba1abd56c3', 9, '2개'),
                                 ('e433ef27-9423-4aeb-8684-a4ba1abd56c3', 4, '2쪽'),
                                 ('e433ef27-9423-4aeb-8684-a4ba1abd56c3', 21, '1T');

-- 27. 왼손으로 먹어도 용서되는 치킨 커리
INSERT INTO RecipeIngredient VALUES
                                 ('e637ae4c-7669-4030-bf78-38bd784d5770', 8, '150g'),
                                 ('e637ae4c-7669-4030-bf78-38bd784d5770', 3, '1개'),
                                 ('e637ae4c-7669-4030-bf78-38bd784d5770', 2, '1/2개'),
                                 ('e637ae4c-7669-4030-bf78-38bd784d5770', 19, '2T');

-- 28. 유학 시절 아플때마다 먹은 소고기 스튜
INSERT INTO RecipeIngredient VALUES
                                 ('f07a9ced-27aa-4a99-834d-a90fb48892af', 6, '200g'),
                                 ('f07a9ced-27aa-4a99-834d-a90fb48892af', 3, '2개'),
                                 ('f07a9ced-27aa-4a99-834d-a90fb48892af', 1, '1개'),
                                 ('f07a9ced-27aa-4a99-834d-a90fb48892af', 28, '1개');

-- 29. 할머니 배불러요..애플파이
INSERT INTO RecipeIngredient VALUES
                                 ('f4706f3b-9000-45d8-8f60-a00ea47db4f1', 32, '1개'),
                                 ('f4706f3b-9000-45d8-8f60-a00ea47db4f1', 12, '50g'),
                                 ('f4706f3b-9000-45d8-8f60-a00ea47db4f1', 21, '2T'),
                                 ('f4706f3b-9000-45d8-8f60-a00ea47db4f1', 11, '100g');

-- 30. 와인/맥주 안주로 딱인 브루스케타
INSERT INTO RecipeIngredient VALUES
                                 ('f873c4c4-599b-4f3a-99e7-e5b571219547', 26, '1조각'),
                                 ('f873c4c4-599b-4f3a-99e7-e5b571219547', 28, '1개'),
                                 ('f873c4c4-599b-4f3a-99e7-e5b571219547', 25, '1T'),
                                 ('f873c4c4-599b-4f3a-99e7-e5b571219547', 22, '약간');


