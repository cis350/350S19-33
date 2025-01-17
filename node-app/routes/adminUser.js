/* Routes for user accounts */
var session = require('client-sessions');

const AdminUser = require('../data/AdminUser.js');
const Status = require('../data/Status.js');
const Report = require('../data/Report.js');

const getLogin = function(req, res) {
  if (req.query.q == 1) {
	  res.render('login', {message: 'Email and password cannot be empty'});
  } else if (req.query.q == 2) {
	  res.render('login', {message: 'No such user'});
  } else if (req.query.q == 3) {
	  res.render('login', {message: 'Wrong password for email'});
  } else {
	  res.render('login', {message: null});
  }
};

const checkLogin = function(req, res) {
  const email = req.body.email;
  const password = req.body.password;

  if (!email || !password) {
    res.redirect('login/?q=1');
  }

  const queryObject = { "email" : email };
    
  AdminUser.findOne( queryObject, (err, person) => {
    if (err) {
      console.log('uh oh' + err);
      res.json({});
    }
    else if (!person) {
      res.redirect('login/?q=2');
    }
    else {
      if (person.password == password) {
        req.session.user = person;
        res.redirect('dashboard/');
      } else {
        res.redirect('login/?q=3');
      }
    }
  });
};

const signup = function(req, res) {
	const email = req.body.email;
	const password = req.body.password;
  const name = req.body.name;
  const school = req.body.school;
  const location = req.body.location;
  const phone = req.body.phone;
  const gender = req.body.gender;
  const role = req.body.role;

  const newUser = new AdminUser({
    email: email,
    password: password,
    name: name,
    school: school,
    openReports: [],
    closedReports: [],
    location: location,
    phone: phone,
    gender: gender,
    role: role,
  });

  const newStatus = new Status({
    email: email,
    name: name,
    onVacation: false
  });

  // save new user to database
  newUser.save( (err) => { 
    if (err) {
      res.redirect('signup/?q=1');
    }
    else {
      newStatus.save( (err) => {
        if(err){
          res.redirect('signup/?q=1');
        } else {
          req.session.user = newUser;
          res.redirect('dashboard/');
        }
      });
    }
  }); 
};

const showSignup = function(req, res) {
	if (req.query.q == 1) {
    res.render('signup', {message: 'This email is already registered'});
  } else {
    res.render('signup', {message: null});
  }
};

const getAdmins = function(req, res) {
    AdminUser.find((err, admins) => {
        if (err) {
          res.send({"result": "error"});
        }
        else {
          res.send({"result": admins});
        }
    });
  };

const getDashboard = function(req, res) {
  if (!req.session.user) {
    res.redirect('login/');
    return;
  }

  const email = req.session.user.email;

  const queryObject = { "email" : email };

  AdminUser.findOne( queryObject, (err, person) => {
    if (err) {
      console.log('uh oh' + err);
      res.json({});
    }
    else if (!person) {
      res.redirect('login/');
    }
    else {
      res.render('dashboard');
    }
  });
};

const getDashboardData = function(req, res) {
  const email = req.session.user.email;

  const queryObject = { 'adminEmail' : email };

  Report.find( queryObject, (err, reports) => {
    if (err) {
      console.log('uh oh' + err);
      res.json({});
    }
    else if (!reports || !reports.length) {
      const data = {
        openReports: 0,
        closedReports: 0,
        dateOpened: [],
        dateClosed: [],
      }
      res.send(data);
    }
    else {
      var openReports = 0;
      var closedReports = 0;
      var dateOpened = [];
      var dateClosed = [];
      for (var i = 0; i < reports.length; i++) {
        const report = reports[i].toObject();
        const reportClosed = report.closed;
        if (reportClosed) {
          closedReports++;
          const closedDate = report.closedDate;
          dateClosed.push(closedDate);
        } else {
          openReports++;
        }
        const date = report.date;
        dateOpened.push(date);
      }
      const data = {
        openReports: openReports,
        closedReports: closedReports,
        dateOpened: processDates(dateOpened),
        dateClosed: processDates(dateClosed),
      }
      res.send(data);
    }
  });
};

const processDates = function(dates) {
  data = [0, 0, 0, 0];
  for (var i = 0; i < dates.length; i++) {
    const month = new Date(dates[i]).getMonth();
    data[month] = data[month] + 1;
  }
  return data;
}

const logOut = function(req, res) {
  req.session.destroy();
  res.redirect('login/');
};

const routes = {
  get_login: getLogin,
  check_login: checkLogin,
  signup: signup,
  show_signup: showSignup,
  get_dashboard: getDashboard,
  get_dashboard_data: getDashboardData,
  log_out: logOut,
  get_admins: getAdmins
};

module.exports = routes;
