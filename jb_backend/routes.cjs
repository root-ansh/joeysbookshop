
class AppPaths {
  constructor(path, action) {
    this.path = path
    this.action = action

  }
}

class AppError {
  constructor(status, message) {
    this.status = status
    this.message = message
  }
}
const filePath = "./server_data/booklist.json"


function getAllBooksList() {
  //const type = "utf8"
  const jsonBuffer = fsLib.readFileSync(filePath)
  // noinspection UnnecessaryLocalVariableJS,JSCheckFunctionSignatures
  const jsonObjArray = JSON.parse(jsonBuffer)
  return jsonObjArray
}

const getApis = Array()
const postApis = Array()

//=============================================================================================

const fsLib = require("fs")
const pathsLib = require("path")
const express = require('express')
const app = express()
const port = process.env.PORT || 3000

const development  = "localhost"
const production = '0.0.0.0'
const host = (process.env.NODE_ENV ? production : development);
console.log(`app is willbe requesting to run at   hostname ${host} :  port ${port}!`)
app.listen(port, host, () => console.log(`app is running on hostname ${host} :  port ${port}!`))

//===============================================================================================
//pre-interceptors

app.use(express.json()) //for consuming json send via post
app.use(express.static("static")) // for rendering  images from static folder without creating a separate route

//===============================================================================================================================================



const homePath = new AppPaths()
homePath.path = "/"
homePath.action = (req, resp) => {

  resp.status(200).sendfile(pathsLib.join(__dirname, './website/index.html'))
}
getApis.push(homePath)


//------------------------------------------------------------------------------------------------

//https://baconipsum.com/api/?type=meat-and-filler&paras=1&format=text

const apiPage = new AppPaths("/api")
apiPage.action = (req, resp) => {
  const getApiPaths = getApis.map((apis) => { return apis.path })
  const postApiPaths = postApis.map((apis) => { return apis.path })
  resp.status(200).json({ msg: "welcome to this awesome website", "get": getApiPaths, "post": postApiPaths })
}
getApis.push(apiPage)

//---------------------------------------------------------------------------------------------------------------------

const allBooks = new AppPaths()
allBooks.path = "/api/booklist"
allBooks.action = (req, resp) => {
  try {

    const jsonObjArray = getAllBooksList()
    let filteredArray = []
    const {title, country, minPages, author, price, language} = req.query;
    if (language) {
      let filterMeArray
      if (filteredArray.length !== 0) { filterMeArray = filteredArray } else { filterMeArray = jsonObjArray }
      filteredArray = filterMeArray.filter((book) => book.language.toLowerCase().indexOf(language.toLowerCase())>=0)
    }
    if (price) {
      let filterMeArray
      if (filteredArray.length !== 0) { filterMeArray = filteredArray } else { filterMeArray = jsonObjArray }
      filteredArray = filterMeArray.filter((book) => book.price.toLowerCase().indexOf(price.toLowerCase())>=0)
    }
    if (country) {
      let filterMeArray
      if (filteredArray.length !== 0) { filterMeArray = filteredArray } else { filterMeArray = jsonObjArray }
      filteredArray = filterMeArray.filter((book) => book.country.toLowerCase().indexOf(country.toLowerCase())>=0)

    }

    if (author) {
      let filterMeArray
      if (filteredArray.length !== 0) { filterMeArray = filteredArray } else { filterMeArray = jsonObjArray }
      filteredArray = filterMeArray.filter((book) => book.author.toLowerCase().indexOf(author.toLowerCase())>=0)
    }

    if (title) {
      let filterMeArray
      if (filteredArray.length !== 0) { filterMeArray = filteredArray } else { filterMeArray = jsonObjArray }
      filteredArray = filterMeArray.filter((book) => book.title.toLowerCase().indexOf(title.toLowerCase())>=0)
    }

    if (minPages) {
      let filterMeArray
      if (filteredArray.length !== 0) { filterMeArray = filteredArray } else { filterMeArray = jsonObjArray }
      filteredArray = filterMeArray.filter((book) => { return book.pages >= minPages })
    }

    const obj = req.query

    if (obj && Object.keys(obj).length === 0 && obj.constructor === Object) {
      resp.status(200).json(jsonObjArray)
    }
    else {
      console.log("query=", req.query)
      resp.status(200).json(filteredArray)
    }


  }
  catch (t) {
    console.error(t.stack);
    resp.status(500).json(new AppError("fail", t.message))
  }
}
getApis.push(allBooks)

// ------------------------------------------------------------------------------------------------
const filteredBooksByPath = new AppPaths()
filteredBooksByPath.path = "/api/booklist/:language"
filteredBooksByPath.action = (req, resp) => {
  try {
    const jsonObjArray = getAllBooksList()
    if (req.params.language) {
      const matchingObjects = jsonObjArray.filter((book) => {
        return book.language === req.params.language
      })
      resp.status(200).json(matchingObjects)
    }

    else {
      resp.status(200).json(jsonObjArray)
    }


  }
  catch (t) {
    resp.status(500).json(new AppError("fail", t.message))
  }
}
getApis.push(filteredBooksByPath)

//------------------------------------------------------------------------------------------------

const updateBookDetails = new AppPaths()
updateBookDetails.path = "/api/update_book"
updateBookDetails.action = (req, resp) => {
  try {
    const booksList = getAllBooksList()
    console.log("req query:", req.query)
    console.log("req params", req.params)
    console.log("req  body:", req.body)
    if (req.body.id == null) {
      resp.status(400).json(new AppError("fail", "id not send"))
    }
    else {
      booksList.forEach((book) => {
        if (book.id === req.body.id) {
          if (req.body.author) book.author = req.body.author
          if (req.body.country) book.country = req.body.country
          if (req.body.language) book.language = req.body.language
          if (req.body.link) book.link = req.body.link
          if (req.body.pages) book.pages = req.body.pages
          if (req.body.title) book.title = req.body.title
          if (req.body.year) book.year = req.body.year
          if (req.body.price) book.price = req.body.price
        }
      })

      fsLib.writeFileSync(filePath, JSON.stringify(booksList, null, 4))
      resp.status(200).json({ "success": true })

    }
  }
  catch (t) {
    resp.status(500).json(new AppError("fail", t.message))
  }

}
postApis.push(updateBookDetails)

//------------------------------------------------------------------------------------------------

const deleteBookDetails = new AppPaths()
deleteBookDetails.path = "/api/delete_book"
deleteBookDetails.action = (req, resp) => {
  try {
    const booksList = getAllBooksList()
    if (req.body.id == null) {
      resp.status(400).json(new AppError("fail", "id not send"))
    }
    else {
      const filteredList = booksList.filter((book) => book.id !== req.body.id)
      fsLib.writeFileSync(filePath, JSON.stringify(filteredList, null, 4))
      resp.status(200).json({ "success": true })
    }
  }
  catch (t) {
    resp.status(500).json(new AppError("fail", t.message))
  }

}
postApis.push(deleteBookDetails)



//================================================================================================================================================

getApis.forEach((appPath) => app.get(appPath.path, appPath.action))
postApis.forEach((appPath) => app.post(appPath.path, appPath.action))

//================================================================================================================================================
//post-interceptors

app.use( (req, res, _) => { // for sending 404 error
  const useJson = false
  if (req.accepts('json') && useJson) {
    res.status(404).json({ error: 'we do not support this route. Not found', request: req.params })
  }
  else {
    res.status(404).sendFile(pathsLib.join(__dirname, './website/404.html'))
  }
})


//================================================================================================================================================





// app.use( (err, req, res, next) =>{
//   console.error(err.stack)

//   req.status = 500
//   // respond with json
//   if (req.accepts('json')) {
//     res.json({ error: 'we do not support this route. Not found' })
//   }
//   else {
//     res.type('txt').send('nah, Not found')
//   }
// })



/*

var bodyParser = require('body-parser')
app.use(bodyParser.json())

app.delete('/products/:id', function(req, res) {
  const { id } = req.params
  res.send(`Delete record with id id`)
})

//-----

app.get('/', (req, res) => {
  res.send('GET request to the homepage')
})

//-----

app.post('/', function (req, res) {
  res.send('POST request to the homepage')
})

//-----

var bodyParser = require('body-parser')
app.use(bodyParser.json())

app.post('/update', function(req, res) {
  const { name, description } = req.body
  res.send(`Name name, desc description`)
})


//-----

app.use(express.json())
app.listen(8080)

app.post('/test', (req, res) => {
  res.json({ body: req.body })
})

//----

var bodyParser = require('body-parser')
app.use(bodyParser.json())

app.put('/products', function(req, res) {
  const { id, name, description } = req.body
  res.send(`Name id name, desc description`)
})

//----

var bodyParser = require('body-parser')
app.use(bodyParser.json())

// for routes looking like this `/products?page=1&pageSize=50`
app.get('/products', function(req, res) {
  const page = req.query.page
  const pageSize = req.query.pageSize
  res.send(`Filter with parameters page and pageSize)`
})

//----

const Joi = require('joi');
const loginSchema = Joi.object().keys({
  username: Joi.string().min(3),
    .max(10),
    .required(),
  password: Joi.string().regex(/^[a-zA-Z0-9]{3,30}$/)
});

app.post('/login', function(req, res) {
  const valid = Joi.validate(req.body, loginSchema).error === null;
  if (!valid) {
    res.status(422).json({
      status: 'error'
      message: 'Invalid request data'
      data: req.body
    });
  } else {
    // happy days - login user
    res.send(`ok`);
  }
});

 */