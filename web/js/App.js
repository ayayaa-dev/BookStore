import {viewModule} from './ViewModule.js';

export {showBtnLogin, showBtnsMenu, hiddenBtnLogin, hiddenBtnsMenu};
var debug = true;
function isDebug(message){
    if(debug) console.log(message);
}
const menuBookShop = document.getElementById("menu_book_shop");
menuBookShop.addEventListener('click', e => {
    e.preventDefault();
    deactiveMenu(menuBookShop);
});
const menuAddAuthor = document.getElementById("menu_add_author");
menuAddAuthor.addEventListener('click', e => {
    e.preventDefault();
    activeBtnMenu(menuAddAuthor);
    viewModule.showNewAuthorForm();
});
const menuAddBook = document.getElementById("menu_add_book");
menuAddBook.addEventListener('click', e=>{
    e.preventDefault();
    activeBtnMenu(menuAddBook);
});
const menuPurchaces = document.getElementById("menu_purchaces");
menuPurchaces.addEventListener('click', e => {
    e.preventDefault();
    activeBtnMenu(menuPurchaces);
});
const menuProfile = document.getElementById("menu_profile");
menuProfile.addEventListener('click', e => {
    e.preventDefault();
    activeBtnMenu(menuProfile);
});
const menuAdminPanel = document.getElementById("menu_admin_panel");
menuAdminPanel.addEventListener('click', e => {
    e.preventDefault();
    activeBtnMenu(menuAdminPanel);
});
const infoElement = document.getElementById("info");
const menuLogin = document.getElementById("menu_login");
menuLogin.addEventListener('click',toggleBtnLogin);
const menuLogout = document.getElementById("menu_logout");
menuLogout.addEventListener('click',toggleBtnLogin);

function toggleBtnLogin(){
    isDebug("Переключаем меню входа")
    if(menuLogin.classList.contains("d-none")){
        showBtnLogin();
        toggleShowMenu();
        infoElement.innerHTML = "Вы вышли";
    }else{
        viewModule.showLoginForm();
//        hiddenBtnLogin();
//        toggleShowMenu();
//        infoElement.innerHTML = "Вы вошли";
    }
}
function showBtnLogin(){
    isDebug("Показываем кнопку Вход");
    menuLogin.classList.remove("d-none");
    menuLogout.classList.add("d-none");
}
function hiddenBtnLogin(){
    isDebug("Прячем кнопку Вход")
    menuLogin.classList.add("d-none");
    menuLogout.classList.remove("d-none");
    
}

function toggleShowMenu(){
    if(menuAddAuthor.classList.contains("d-none")){
        showBtnsMenu();
    }else{
        hiddenBtnsMenu();
    }
}
function showBtnsMenu(){
    menuAddAuthor.classList.remove("d-none");
    menuAddBook.classList.remove("d-none");
    menuPurchaces.classList.remove("d-none");
    menuProfile.classList.remove("d-none");
    menuAdminPanel.classList.remove("d-none");
}
function hiddenBtnsMenu(){
    menuAddAuthor.classList.add("d-none");
    menuAddBook.classList.add("d-none");
    menuPurchaces.classList.add("d-none");
    menuProfile.classList.add("d-none");
    menuAdminPanel.classList.add("d-none");
}
function activeBtnMenu(activeMenuBtn){
    if(!activeMenuBtn.classList.contains("active")){
        activeMenuBtn.classList.add("active");
    }
    deactiveMenu(activeMenuBtn);
}
function deactiveMenu(activeMenuBtn){
    const listNavLinks = document.getElementsByClassName('nav-link');
    for(let i = 0; i < listNavLinks.length; i++){
        if(listNavLinks[i] !== activeMenuBtn && listNavLinks[i].classList.contains('active')){
            listNavLinks[i].classList.remove('active');
        }
    }
}


