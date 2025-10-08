// Login Mode Dom Selector
const modeContainer = document.getElementById('loginMode');
const idLogin = document.getElementById('idLogin');
const emailLogin = document.getElementById('emailLogin');
const loginForm = document.getElementById("loginForm");
const loginIdInput = document.getElementById("loginIdInput");
const loginIdLabel = document.getElementById("loginIdLabel");
// Login Mode 동작부
idLogin.addEventListener('click', () => {
  modeContainer.classList.remove('email-active');
  loginForm.setAttribute("action","/login?action=username-login");
  loginIdInput.setAttribute("type", "text");
  loginIdInput.setAttribute("placeholder", "유저이름을 입력해주세요.");
  loginIdLabel.innerText = "Username";
});

emailLogin.addEventListener('click', () => {
  modeContainer.classList.add('email-active');
  loginForm.setAttribute("action","/login?action=email-login");
  loginIdInput.setAttribute("type", "email");
  loginIdInput.setAttribute("placeholder", "이메일을 입력해주세요.");
  loginIdLabel.innerText = "E-Mail"; 
});