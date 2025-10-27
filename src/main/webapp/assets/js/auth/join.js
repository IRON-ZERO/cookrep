// Kakao Address Mode Selector
const addressLabel = document.querySelector(".join-page__addressBtn-cont--header > label");
const joinCountryInput = document.getElementById("joinCountryInput");
const joinCityInput = document.getElementById("joinCityInput");
const joinAddressBtn = document.getElementById("joinAddressBtn");
// Self Address Mode Selector
const joinAddressSelfCheckbox = document.getElementById("joinAddressSelfCheckbox");
const joinAddressSelfLabel = document.getElementById("joinAddressSelfLabel");
const checkboxUIbg = document.querySelector(".join-page__address-cont--checkbox-bg");
const checkboxUI = document.querySelector(".join-page__address-cont--checkbox-box");

/**
 * 카카오 Address API
 */
function onClickAddressBtn() {
  new daum.Postcode({
    oncomplete: function(data) {
      joinCountryInput.setAttribute("value", "대한민국");
      joinCityInput.setAttribute("value", `${data.roadAddress}`);
    }
  }).open();
}

function onChangeAddressSelfBtn(){
  const check = joinAddressSelfCheckbox.checked;
   joinCityInput.disabled = !check;
   joinCountryInput.disabled = !check;
   joinAddressBtn.disabled = check;
   if (check) {
     checkboxUIbg.classList.add("address-self--active");
     checkboxUI.classList.add("address-self--activeBtn");
     addressLabel.removeEventListener("click", onClickAddressBtn);
     joinAddressBtn.removeEventListener("click", onClickAddressBtn);
   } else {
     checkboxUIbg.classList.remove("address-self--active");
     checkboxUI.classList.remove("address-self--activeBtn");
     addressLabel.addEventListener("click", onClickAddressBtn);
     joinAddressBtn.addEventListener("click", onClickAddressBtn);
   }
}

addressLabel.addEventListener("click", onClickAddressBtn);
joinAddressBtn.addEventListener("click",onClickAddressBtn);
joinAddressSelfCheckbox.addEventListener("change", onChangeAddressSelfBtn);