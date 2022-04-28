import {checkMenu} from './App.js';
class LoginModule{
    
    sendCredential(){
        const login = document.getElementById('login').value;
        const password = document.getElementById('password').value;
        const credendial = {
            "login": login,
            "password": password
        };
        //Посылаем запрос а с паттерном 'login', методом POST и телом body в формате JSON
        // возвращается обещание (Promise) со статусом "ожидание"
        let promise = fetch('login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            },
            credentials: 'include',
            body: JSON.stringify(credendial) 
        });
        // Обрабатываем обещание с помощью then
        promise.then(response => response.json()) //переводим обещание в статус выполнено 
                                                  // и преобразовываем JSON в JavaScript object
               .then(response => {// обрабатываем object полученый из обещания
                    document.getElementById('info').innerHTML = response.info;
                    if(response.auth){
                        sessionStorage.setItem('user',JSON.stringify(response.user));
                        sessionStorage.setItem('role',JSON.stringify(response.role));
                        checkMenu();
                        document.getElementById('content').innerHTML = "";
                    }
               })
               .catch(error =>{
                    document.getElementById('info').innerHTML = "Ошибка сервера: "+error;
                    checkMenu();
                    document.getElementById('content').innerHTML = "";
               });


    }
    logout(){
        let promiseLogout = fetch('logout', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            },
            credentials: 'include'
        });
        // Обрабатываем обещание с помощью then
        promiseLogout.then(response => response.json()) 
           .then(response => {// обрабатываем object полученый из обещания
                document.getElementById('info').innerHTML = response.info;
                if(!response.auth){
                    if(sessionStorage.getItem('user')){
                        sessionStorage.removeItem('user');
                    }
                    if(sessionStorage.getItem('role')){
                        sessionStorage.removeItem('role');
                    }
                   checkMenu();
                }
            });
    }
}

const loginModule = new LoginModule();

export {loginModule};

