
const infoElement = document.getElementById("info");
const menuLogin = document.getElementById("menu_login");
menuLogin.addEventListener('click',toggleBtnLogin);
const menuLogout = document.getElementById("menu_logout");
menuLogout.addEventListener('click',toggleBtnLogin);

function toggleBtnLogin(){
    if(menuLogin.classList.contains("d-none")){
        menuLogin.classList.remove("d-none");
        menuLogout.classList.add("d-none");
        infoElement.innerHTML = "&nbsp;Вы вышли";
    }else{
        menuLogin.classList.add("d-none");
        menuLogout.classList.remove("d-none");
        infoElement.innerHTML = "&nbsp;Вы вошли";
    }
}