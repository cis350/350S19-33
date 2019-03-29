// set up Express
var express = require('express');
var app = express();

// set up EJS
app.set('view engine', 'ejs');

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

const user = require('./routes/user.js');

/***************************************/

app.get('/login', user.get_login);
app.post('/checkLogin', user.check_login);
app.get('/signup', user.show_signup);
app.post('/signup', user.signup);
app.get('/dashboard', user.get_dashboard);
app.get('/dashboarddata', user.get_dashboard_data);

/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('login'); } );

app.listen(3000,  () => {
	console.log('Listening on port 3000');
    });
