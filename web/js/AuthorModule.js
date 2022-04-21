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
                });
                        
    }
    insertListAuthors(){
        const promiseListAuthors = fetch('getListAuthors',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            }
        });
        promiseListAuthors
                .then(response => response.json())
                .then(response =>{
                    if(response.status){
                        const select = document.getElementById('select_authors');
                        select.options.length=0;
                        for(let i=0; i<response.authors.length; i++){
                            const option = document.createElement('option');
                            option.text = response.authors[i].firstname+' '+response.authors[i].lastname;
                            option.value = response.authors[i].id;
                            select.add(option);
                        }
                    }else{
                       document.getElementById('info').innerHTML = response.info;  
                    }
                })
                .catch(error=>{
                    document.getElementById('info').innerHTML = 'Ошибка сервера: '+error;
                });
    }
    editAuthor(){
        const authorId = document.getElementById('select_authors').value;
        const object = {
            "authorId":authorId
        }
        const promiseAuthor = fetch('getAuthor',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            },
            body: JSON.stringify(object)
        });
        promiseAuthor
                .then(response => response.json())
                .then(response =>{
                   if(response.status){
                       document.getElementById('info').value = response.info;
                       document.getElementById('author_id').value = response.author.id
                       document.getElementById('firstname').value = response.author.firstname;
                       document.getElementById('lastname').value = response.author.lastname;
                       document.getElementById('birth_year').value = response.author.birthYear;
                   }else{
                       document.getElementById('info').value = response.info;
                   }
                })
                .catch(error=>{
                    document.getElementById('info').innerHTML = 'Ошибка сервера: '+error;
                });
    }
    updateAuthor(){
        const authorId = document.getElementById("author_id").value;
        const firstname = document.getElementById("firstname").value;
        const lastname = document.getElementById("lastname").value;
        const birthYear = document.getElementById("birth_year").value;
        const updateUser = {
            "authorId": authorId,
            "firstname": firstname,
            "lastname": lastname,
            "birthYear": birthYear,
        };
        const promiseAuthor = fetch('updateAuthor',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            },
            body: JSON.stringify(updateUser)
        });
        promiseAuthor
                .then(response => response.json())
                .then(response =>{
                   if(response.status){
                       document.getElementById('info').innerHTML = 'Автор изменен';
                       viewModule.showNewAuthorForm();
                   }else{
                       document.getElementById('info').innerHTML = 'Автора изменить не удалось';
                   }
                })
                .catch(error=>{
                    document.getElementById('info').innerHTML = 'Ошибка сервера: '+error;
                });
    }
}
const authorModule = new AuthorModule();

export {authorModule};