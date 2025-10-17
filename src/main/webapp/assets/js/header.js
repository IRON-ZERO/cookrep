const main = document.querySelector("main")
const header = document.querySelector("header")
const globalNavList = document.querySelectorAll("#globalNav > ul > li > a");
const logo = document.getElementById("headerLogo");
const path = window.location.pathname;

globalNavList.forEach((val, key, node) => {
  if (val.pathname === path) {
    val.classList.add("nav-active");
  } else {
    val.classList.remove("nav-active");
  }
})

window.addEventListener("scroll", (event) => {
  const yOffset = window.pageYOffset;
  if (yOffset > 100) {
    if (main) {
      main.classList.add("small");
    }
    if (header) {
      header.classList.add("small");
    }
    if (logo) {
      logo.classList.add("small");
    }
  } else {
    if (main) {
      main.classList.remove("small");
    }
    if (header) {
      header.classList.remove("small");
    }
    if (logo) {
      logo.classList.remove("small");
    }
  }
})