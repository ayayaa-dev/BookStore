import {viewModule} from './ViewModule.js';
class AuthorModule{
    createNewAuthor(){
        const firstname = document.getElementById('firstname').value;
        const lastname = document.getElementById('lastname').value;
        const birthYear = document.getElementById('birth_year').value;
        const newAuthor = {
            "firstname": firstname,
            "lastname": lastname,
            "birthYear": birthYear
        }
        const promise = fetch('createNewAuthor',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            },
            body: JSON.stringify(newAuthor) 
        });
        promise.then(response => response.json())
               .then(response =>{
                   if(response.status){
                       document.getElementById('info').innerHTML = response.info;
                       viewModule.showNewAuthorForm();
                   }else{
                       document.getElementById('info').innerHTML = response.info;
                       firstname = response.firstname;
                       lastname = response.lastname;
                       birthYear = response.birthYear;
                   }
                })
                .catch(error=>{
                    document.getElementById('info').innerHTML = 'Ошибка сервера: '+error;
                })
                        
    }
}
const authorModule = new AuthorModule();

export {authorModule};