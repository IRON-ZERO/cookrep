use cookrep;
-- 사용자 테이블
CREATE TABLE User (
                      user_id VARCHAR(30) PRIMARY KEY,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      nickname VARCHAR(20),
                      first_name VARCHAR(20),
                      last_name VARCHAR(20),
                      country VARCHAR(50),
                      city  VARCHAR(50),
                      email VARCHAR(100),
                      password VARCHAR(20),

);

-- 재료 테이블
CREATE TABLE Ingredient (
                            ingredient_id INT AUTO_INCREMENT PRIMARY KEY,   -- INT로 통일
                            name VARCHAR(50) NOT NULL,                       -- 이름 길이 여유 있게
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 사용자-재료 관계 (사용자 냉장고)
CREATE TABLE userIngredient (
                                user_id VARCHAR(30) NOT NULL,                   -- users.user_id와 타입 맞춤
                                ingredient_id INT NOT NULL,                      -- ingredient_id와 타입 맞춤
                                PRIMARY KEY (user_id, ingredient_id),
                                FOREIGN KEY (user_id) REFERENCES User(user_id),
                                FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id)
);

-- 레시피 테이블
CREATE TABLE Recipe (
                        recipe_id VARCHAR(20) PRIMARY KEY,  -- 예: 20251001_001
                        user_id VARCHAR(30),
                        title VARCHAR(100),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        thumbnail_image_url VARCHAR(100),  -- S3 업로드 경로 (썸네일용 완성 이미지)
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
                             recipe_id VARCHAR(20) NOT NULL,
                             step_order INT NOT NULL,  -- 단계 순서
                             contents TEXT,    -- 긴 글 저장
                             image_url VARCHAR(100),  -- S3 업로드 경로
                             FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id)
);

-- 레시피-재료 테이블 (사용 재료)
CREATE TABLE RecipeIngredient (
                                  recipe_id VARCHAR(20),
                                  ingredient_id INT,
                                  count VARCHAR(20), -- 사용한 수량(단위랑 합쳐서 문자열)
                                  PRIMARY KEY (recipe_id, ingredient_id),
                                  FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id),
                                  FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id)
);

-- 댓글 테이블
CREATE TABLE Comment (
                         comment_id INT AUTO_INCREMENT PRIMARY KEY,   -- 댓글 수 많지 않으면 INT 충분
                         user_id VARCHAR(30) NOT NULL,
                         recipe_id VARCHAR(20) NOT NULL,
                         contents TEXT NOT NULL,                      -- 긴 글은 TEXT
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES User(user_id),
                         FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id)
);

-- 스크랩 테이블
CREATE TABLE Scrap (
                       recipe_id VARCHAR(20) NOT NULL,
                       user_id VARCHAR(30) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       PRIMARY KEY (recipe_id, user_id),
                       FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id),
                       FOREIGN KEY (user_id) REFERENCES User(user_id)
);
