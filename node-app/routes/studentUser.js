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


const changeInfo = function(req, res) {
  const username = req.query.username;
  const password = req.query.password;
  const school = req.query.school;
  const age = req.query.age;
  const gender = req.query.gender;

  const queryObject = { "username" : username };
    
  StudentUser.findOne( queryObject, (err, person) => {
    if (err) {
      res.send({"result": false});
    }
    else if (!person) {
      res.send({"result": "no such user"});
    }
    else {
      if(password != "" || !password){
        person.password = password;
      }
      if(school != "" || !school){
        person.school = school;
      }
      if(age != "" || !age){
        person.age = age;
      }
      if(gender != "" || !gender){
        person.gender = gender;
      }
       person.save((err) => {
              if(err){
                res.type('html').status(500);
               res.send({"result": 'error'});
             } else {
              res.send({"result": 'edited'});
      }
    });
     }
  });
};

const routes = {
  check_login: checkLogin,
  signup: signup,
  change_info: changeInfo
};

module.exports = routes;
