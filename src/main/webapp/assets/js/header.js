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
    main.classList.add("small");
    header.classList.add("small");
    logo.classList.add("small");
  } else {
    main.classList.remove("small");
    header.classList.remove("small");
    logo.classList.remove("small");
  }
})