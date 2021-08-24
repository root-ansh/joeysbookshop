
![website](static/images/website.png)

## JB BACKEND

The Backend is a Simple Node JS Based Server with routes created via express. It uses a Json file as Database . it is hosted on a heroku [server](https://joeysbookshop.herokuapp.com/)

## apis

(Note: All apis return json if not otherwise mentioned)

> GET 

- `https://joeysbookshop.herokuapp.com/` : homepage(html, served as website)
- `https://joeysbookshop.herokuapp.com/apis` : Api listing page
- `https://joeysbookshop.herokuapp.com/apis/booklist` : all books. note: this api can also be used for filtering via many query params like `?language=English&title=hamlet` 
- `https://joeysbookshop.herokuapp.com/apis/booklist/<bookid>` : will provide a single book 

> POST

- `https://joeysbookshop.herokuapp.com/apis/update_book` : `{"price": "$37",   "language": "Punjabi","pages": 999,"title": "Fairy tales by jaggi singh","id": "78566a8d"}` returns true for successful update otherwise error+false.  


- `https://joeysbookshop.herokuapp.com/apis/delete_book` : `{"price": "$37",   "language": "Punjabi","pages": 999,"title": "Fairy tales by jaggi singh","id": "78566a8d"}` returns true for successful deletion otherwise error+false.  


## Run the project locally:

prerequisites : install node+npm
1. clone project
2. `npm init`
3. `nodemon routes.cjs` : will show the url where server is running in terminal


## Upcoming 

[ ] ktor
[ ] many improvements

