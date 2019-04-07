/* Routes for user accounts */
var session = require('client-sessions');

const StudentUser = require('../data/StudentUser.js');

const checkLogin = function(req, res) {
  const username = req.query.username;
  const password = req.query.password;

  const queryObject = { "username" : username };
    
  StudentUser.findOne( queryObject, (err, person) => {
    if (err) {
      res.send({"result": false});
    }
    else if (!person) {
      res.send({"result": "no such user"});
    }
    else {
      if (person.password == password) {
        res.send({"result": "logged in"});
      } else {
        res.send({"result": "wrong password"});
      }
    }
  });
};

const signup = function(req, res) {
	const username = req.query.username;
	const password = req.query.password;
  const school = req.query.school;
  const age = req.query.age;
  const gender = req.query.gender;

  const newUser = new StudentUser({
    username: username,
    password: password,
    school: school,
    age: age,
    gender: gender,
  });

  // save new user to database
  newUser.save( (err) => { 
    if (err) {
      res.send({"result": "username taken"});
    }
    else {
      res.send({"result": "signed up"});
    }
  }); 
};



const routes = {
  check_login: checkLogin,
  signup: signup,
};

module.exports = routes;
