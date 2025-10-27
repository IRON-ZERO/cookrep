<%@ page contentType="text/html;charset=UTF-8"%>
<aside class="mypage__navigation">
	<a href="/">
		<img alt="로고이미지" src="/assets/images/logos/logo_1.png" />
	</a>
	<div class="profile-box">
		<img src="/assets/images/icons/user-icon-1.png" alt="profile" class="profile-img">
		<h3>${user.nickname}</h3>
		<p>${user.email}</p>
	</div>
	<nav>
		<a href="/mypage" class="active">
			<div>
				<img src="/assets/images/icons/profile_icon.png" alt="프로필_아이콘">
				<p>프로필</p>
			</div>
		</a>
		<a href="/mypage/fridge">
			<div>
				<img src="/assets/images/icons/fridge_icon.png" alt="냉장고_아이콘">
				<span>내 냉장고</span>
			</div>
		</a>
		<a href="/mypage/scrap">
			<div>
				<img src="/assets/images/icons/fridge_icon.png" alt="냉동고_아이콘">
				<span>내 냉동고</span>
			</div>
		</a>
		<a href="/mypage/recipe">
			<div>
				<img src="/assets/images/icons/recipe.png" alt="레시피_아이콘">
				<span>내 레시피</span>
			</div>
		</a>
	</nav>
	<div class="logout"><a href="/logout">로그아웃</a></div>
	<script>
        // 사이드 바 디자인 위한 스크립트
        document.addEventListener("DOMContentLoaded", function () {
            // 현재 URL 경로 (/mypage, /mypage/fridge 등)
            const currentPath = window.location.pathname;

            // 모든 nav 링크 가져오기
            const links = document.querySelectorAll("nav a");

            links.forEach(link => {
                // href 속성값
                const linkPath = link.getAttribute("href");

                // 현재 URL이 href로 시작하면 active 적용
                if (currentPath === linkPath) {
                    link.classList.add("active");
                } else {
                    link.classList.remove("active");
                }
            });
        });
	</script>
</aside>