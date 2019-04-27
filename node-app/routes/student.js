/* Routes for student search */
var session = require('client-sessions');
const ObjectId = require('mongodb').ObjectID;

const Student = require('../data/StudentUser.js');
const Report = require('../data/Report.js');

const getStudents = function(req, res) {
  Student.find( (err, allStudents) => {
     if (err) {
        res.type('html').status(500);
        res.send('Error: ' + err);
     }
     else if (allStudents.length == 0) {
        res.type('html').status(200);
        res.send('There are no students');
     } else {
        res.render('students', { students: allStudents });
     }
  });
};

const getStudent = function(req, res) {
  const searchUsername = req.query.username;

  Student.findOne( { username: searchUsername }, (err, student) => {
    if (err) {
      res.type('html').status(500);
      res.send('Error: ' + err);
    } else if (!student) {
      res.type('html').status(200);
      res.send('No student with the username ' + searchUsername);
    } else {
      Report.find( { studentUsername : searchUsername }, (err, reports) => {
        if (err) {
          res.type('html').status(500);
          res.send('Error: ' + err);
        } else if (!reports) {
          res.render('student', { student: student});
        } else {
          res.render('student', { student: student, reports: reports });
        }
      });
    }
  });
};
    
const getReportsForOneStudent = function(req, res) {
  const searchUsername = req.query.username;

  Student.findOne( { username: searchUsername }, (err, student) => {
    if (err) {
      res.type('html').status(500);
      res.send('error');
    } else if (!student) {
      res.type('html').status(200);
      res.send('No student with the username');
    } else {
      Report.find( { studentUsername : searchUsername }, (err, reports) => {
        if (err) {
          res.type('html').status(500);
          res.send('error');
        } else if (!reports) {
          res.send('empty');
        } else {
          res.render({ 'reports': reports });
        }
      });
    }
  });
};

const saveStudent = function(req, res) {
  const username = req.body.username;
  const name = req.body.name;
  const age = req.body.age;
  const gender = req.body.gender;
  const school = req.body.school;

  const newStudent = new Student({
    username: username,
    name: name,
    age: age,
    gender: gender,
    school: school
  });

  newStudent.save( (err) => {
    if (err) {
      res.send('Error: ' + err);
    } else {
      res.redirect('/student?username=' + username);
    }
  });
};

const routes = {
  get_students: getStudents,
  get_student: getStudent,
  save_student: saveStudent,
  get_reports_per_student: getReportsForOneStudent
};

module.exports = routes;