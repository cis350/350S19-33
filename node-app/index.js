// set up Express
var express = require('express');
var app = express();

var session = require('client-sessions');

app.use(session({
  cookieName: 'session',
  secret: 'random_string_goes_here',
  duration: 30 * 60 * 1000,
  activeDuration: 5 * 60 * 1000,
}));

// set up EJS
app.set('view engine', 'ejs');

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

const user = require('./routes/user.js');
const status = require('./routes/status.js');

/***************************************/

app.get('/login', user.get_login);
app.post('/checkLogin', user.check_login);
app.get('/signup', user.show_signup);
app.post('/signup', user.signup);
app.get('/dashboard', user.get_dashboard);
app.get('/dashboarddata', user.get_dashboard_data);

//Michelle's status
app.get('/status', status.get_statuses);
app.post('/changed', status.change_status)

/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('login'); } );

app.listen(3000,  () => {
	console.log('Listening on port 3000');
    });
