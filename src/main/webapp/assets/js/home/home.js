const homePagePickList = document.getElementById("homePagePickList");
const homePagePickLists = document.querySelectorAll('.home-page__pick-list > li');
let action = null;
let interval = 0;

function transitonPickList() {
  interval++;
  if (interval === 1) {
    homePagePickList.style.transform = `translateX(-${2000}px)`;
  } else if (interval === 2) {
    homePagePickList.style.transform = `translateX(-${4000}px)`;
  } else if (interval === 3) {
    homePagePickList.style.transform = `translateX(-${6000}px)`;
  } else if (interval === 4) {
    homePagePickList.style.transform = `translateX(-${7500}px)`;
  } else if (interval === 5) {
    homePagePickList.style.transform = `translateX(-${7585}px)`;
  } else {
    interval = 0;
    homePagePickList.style.transform = `translateX(0px)`;
  }
}

function startAction() {
  if (!action) {
    action = setInterval(transitonPickList, 4000);
  }
}
function stopAction() {
  clearInterval(action);
  action = null;
}

startAction();

homePagePickLists.forEach((item) => {
  item.addEventListener("mouseenter", stopAction);
  item.addEventListener("mouseleave", startAction);
})