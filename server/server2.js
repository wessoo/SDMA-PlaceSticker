var app = require('express')()
  , server = require('http').createServer(app)
  , io = require('socket.io').listen(server);

server.listen(80);

app.get('/', function (req, res) {
  res.sendfile(__dirname + '/index.html');
});

app.get('/addimage', function (req, res) {
	io.sockets.emit('add_img', req.query.image);
	console.log("Requested image " + req.query.image);
	res.end("Request for " + req.query.image + " made");
});