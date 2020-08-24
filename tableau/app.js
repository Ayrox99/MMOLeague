const http = require('http');
const path = require('path');
const express = require('express');
const bodyParser = require('body-parser');
const app = express();
app.use(bodyParser.urlencoded({extended: false})); app.use(express.static(path.join(__dirname, 'public')));
app.use((req, res,next)=>{
   res.sendFile(path.join(__dirname, 'public', 'index.html'));
});
const server = http.createServer(app);
server.listen(3000);