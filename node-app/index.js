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
const event = require('./routes/event.js');
const report = require('./routes/report.js');
const student = require('./routes/student.js');

/***************************************/

app.get('/login', user.get_login);
app.post('/checkLogin', user.check_login);
app.get('/signup', user.show_signup);
app.post('/signup', user.signup);
app.get('/dashboard', user.get_dashboard);
app.get('/dashboarddata', user.get_dashboard_data);

//Michelle's status
app.get('/status', status.get_statuses);
app.post('/changed', status.change_status);

//Chelsey's events
app.get('/events', event.show_events);
app.get('/event', event.show_event);
app.use('/createEvent', (req, res) => { res.redirect('/public/eventform.html'); } );
app.get('/editEvent', event.edit_event);
app.get('/deleteEvent', event.delete_event);

var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use('/handleForm', event.save_event, (req, res) => {
	res.send("event form handled");
});
app.use('/editForm', event.update_event, (req, res) => {
	res.send("event form handled");
});

//notification mailbox and individual reports
app.get('/reports', report.get_reports);
app.get('/reports#read', report.get_read);
app.get('/reports#unread', report.get_unread);

//Chelsey's search 
app.get('/students', student.get_students);
app.get('/student', student.get_student);
/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('login'); } );

app.listen(3000,  () => {
	console.log('Listening on port 3000');
    });
