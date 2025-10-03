// Login Mode Dom Selector
const modeContainer = document.getElementById('loginMode');
const idLogin = document.getElementById('idLogin');
const emailLogin = document.getElementById('emailLogin');
const loginIdInput = document.getElementById("loginIdInput");
const loginIdLabel = document.getElementById("loginIdLabel");

// Login Mode 동작부
idLogin.addEventListener('click', () => {
  modeContainer.classList.remove('email-active');
  loginIdInput.setAttribute("name", "nickname");
  loginIdInput.setAttribute("type", "text");
  loginIdInput.setAttribute("placeholder", "유저이름을 입력해주세요.");
  loginIdLabel.innerText = "Username";
});

emailLogin.addEventListener('click', () => {
  modeContainer.classList.add('email-active');
  loginIdInput.setAttribute("name", "email");
  loginIdInput.setAttribute("type", "email");
  loginIdInput.setAttribute("placeholder", "이메일을 입력해주세요.");
  loginIdLabel.innerText = "E-Mail"; 
});