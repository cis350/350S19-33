/* Routes for user accounts */

const User = require('../data/User.js');

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
    
  User.findOne( queryObject, (err, person) => {
    if (err) {
      console.log('uh oh' + err);
      res.json({});
    }
    else if (!person) {
      res.redirect('login/?q=2');
    }
    else {
      if (person.password == password) {
        res.redirect('dashboard/?email=' + email);
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

  const newUser = new User({
    email: email,
    password: password,
    name: name,
    school: school,
  });

  // save new user to database
  newUser.save( (err) => { 
    if (err) {
      res.redirect('signup/?q=1');
    }
    else {
      res.redirect('dashboard/?email=' + email);
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

const dashboard = function(req, res) {
  const email = req.query.email;

  const queryObject = { "email" : email };

  User.findOne( queryObject, (err, person) => {
    if (err) {
      console.log('uh oh' + err);
      res.json({});
    }
    else if (!person) {
      res.redirect('login/');
    }
    else {
      const personObj = person.toObject();
      const openReports = personObj.openReports.length;
      const closedReports = personObj.closedReports.length;
      const data = {
        openReports: openReports,
        closedReports: closedReports,
      }
      res.render('dashboard', {data: data});
    }
  });
};


const routes = {
  get_login: getLogin,
  check_login: checkLogin,
  signup: signup,
  show_signup: showSignup,
  dashboard: dashboard,
};

module.exports = routes;
