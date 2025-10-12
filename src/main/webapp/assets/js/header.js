const globalNavList = document.querySelectorAll("#globalNav > ul > li > a");
const path = window.location.pathname;

globalNavList.forEach((val, key, node) => {
  if (val.pathname === path) {
    val.classList.add("nav-active");
  } else {
    val.classList.remove("nav-active");
  }
})