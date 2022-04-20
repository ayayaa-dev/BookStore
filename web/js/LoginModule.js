import {showBtnLogin, showBtnsMenu, hiddenBtnLogin, hiddenBtnsMenu} from './App.js';
class LoginModule{
    
    sendCredential(){
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
               .catch(error =>{
                    document.getElementById('info').innerHTML = "Ошибка сервера: "+error;
                    hiddenBtnsMenu();
                    showBtnLogin();
                    document.getElementById('content').innerHTML = "";
               })


    }
}

const loginModule = new LoginModule();

export {loginModule};

