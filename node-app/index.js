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

const adminUser = require('./routes/adminUser.js');
const studentUser = require('./routes/studentUser.js');
const status = require('./routes/status.js');
const event = require('./routes/event.js');
const report = require('./routes/report.js');
const student = require('./routes/student.js');

/***************************************/

app.get('/login', adminUser.get_login);
app.post('/checkLogin', adminUser.check_login);
app.get('/signup', adminUser.show_signup);
app.post('/signup', adminUser.signup);
app.get('/dashboard', adminUser.get_dashboard);
app.get('/dashboarddata', adminUser.get_dashboard_data);
app.get('/logout', adminUser.log_out);

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
app.get('/report', report.get_report);
app.get('/editMemo', (req, res) => {res.redirect('/public/memoform.html');});
app.get('/createMemo', (req, res) => {res.redirect('/public/memoform.html');});
app.get('/showMemos', report.show_memos);
app.get('/deleteMemo', report.delete_memo);
app.get('/toClose', report.close_report);
app.get('/closed', report.get_closed);
app.get('/read', report.get_read);
app.get('/unread', report.get_unread);

app.use('/showMForms', report.show_memos, (req, res) => {
    res.send("memo form handled");
})

app.use('/editMForm', report.save_memo, (req, res) => {
	res.send("memo form handled");
});

app.use('/saveForm', report.save_memo, (req, res) => {
    res.send("memo form handled");
});

app.post('/addComment', report.add_comment);

//Chelsey's search 
app.get('/students', student.get_students);
app.get('/student', student.get_student);
app.use('/createStudent', (req, res) => { res.redirect('/public/studentform.html'); } );
app.use('/studentForm', student.save_student, (req, res) => {
	res.send("stuent form handled");
});

// API
app.get('/studentLogin', studentUser.check_login);
app.get('/studentSignup', studentUser.signup);
/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('login'); } );

app.listen(3000,  () => {
	console.log('Listening on port 3000');
    });
