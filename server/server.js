var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);

app.use('/', express.static(__dirname + '/'));

app.get('/addimage', function (req, res) {
	io.sockets.emit('add_img', req.query.image);
	console.log("Requested image " + req.query.image);
	res.end("Request for " + req.query.image + " was made");
});

server.listen(4100);
console.log("Server started");