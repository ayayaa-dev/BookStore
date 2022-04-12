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
        showLoginForm();
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

function showLoginForm(){
    const content = document.getElementById('content');
    content.innerHTML = `
<div class="card border-secondary mb-3 mx-auto" style="max-width: 30rem;">
    <h3 class="card-header w-100 text-center ">Авторизация</h3>
    <div class="card-body">
      <div class="form-group">
        <label for="login" class="form-label mt-4">Логин</label>
        <input type="text" class="form-control" id="login" placeholder="Login">
      </div>
      <div class="form-group">
        <label for="password" class="form-label mt-4">Password</label>
        <input type="password" class="form-control" id="password" placeholder="Password">
      </div>
      <button id='button_login' type="submit" class="btn btn-primary my-3">Войти</button>
    </div>
</div>`;
    const buttonLogin = document.getElementById("button_login");
    buttonLogin.addEventListener('click', (e)=>{
        e.preventDefault();
        sendCredential();
    })
}
function  sendCredential(){
    const login = document.getElementById('login').value;
    const password = document.getElementById('password').value;
    const credendial = {
        "login": login,
        "password": password,
    }
    //Посылаем запрос а с паттерном 'login', методом POST и телом body в формате JSON
    // возвращается обещание (Promise) со статусом "ожидание"
    let promise = fetch('login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset:utf8'
        },
        body: JSON.stringify(credendial) 
    })
    // Обрабатываем обещание с помощью then
    promise.then(response => response.json()) //переводим обещание в статус выполнено 
                                              // и преобразовываем JSON в JavaScript object
           .then(response => {// обрабатываем object полученый из обещания
                document.getElementById('info').innerHTML = response.info;
                if(response.auth){
                    showBtnsMenu();
                    hiddenBtnLogin();
                    document.getElementById('content').innerHTML = "";
                }
           })
           .catch(response =>{
                document.getElementById('info').innerHTML = "Ошибка сервера";
                hiddenBtnsMenu();
                showBtnLogin();
                document.getElementById('content').innerHTML = "";
           })
               
           
}