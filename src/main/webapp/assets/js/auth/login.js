// Login Mode Dom Selector
const modeContainer = document.getElementById('loginMode');
const idLogin = document.getElementById('idLogin');
const emailLogin = document.getElementById('emailLogin');
const loginForm = document.getElementById("loginForm");
const loginIdInput = document.getElementById("loginIdInput");
const loginIdLabel = document.getElementById("loginIdLabel");
const userIcon = document.querySelector(".login-page__input-id > img");
// Password 보이기
const loginPwInput = document.getElementById("loginPwInput");
const eyesIconBtn = document.querySelector(".login-page__input-pw > button");
const eyesIcon = eyesIconBtn.querySelector("img");

// Login Mode 동작부
idLogin.addEventListener('click', () => {
  modeContainer.classList.remove('email-active');
  loginForm.setAttribute("action", "/login?action=username-login");
  loginIdInput.setAttribute("type", "text");
  loginIdInput.setAttribute("placeholder", "유저이름을 입력해주세요.");
  loginIdLabel.innerText = "Username";
  userIcon.src = "/assets/images/icons/user-icon-1.png"
});

emailLogin.addEventListener('click', () => {
  modeContainer.classList.add('email-active');
  loginForm.setAttribute("action", "/login?action=email-login");
  loginIdInput.setAttribute("type", "email");
  loginIdInput.setAttribute("placeholder", "이메일을 입력해주세요.");
  loginIdLabel.innerText = "E-Mail";
  userIcon.src = "/assets/images/icons/email.png"
});

function openEyes() {
  const open = "/assets/images/icons/open-eyes.png";
  loginPwInput.setAttribute("type", "text");
  eyesIcon.src = open;
}
function closeEyes() {
  const close = "/assets/images/icons/closed-eyes.png";
  loginPwInput.setAttribute("type", "password");
  eyesIcon.src = close;
}
eyesIconBtn.addEventListener("click", () => {
  eyesIcon.src.includes("closed-eyes.png") ? openEyes() : closeEyes();
})