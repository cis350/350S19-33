/* Routes for student search */
var session = require('client-sessions');
const ObjectId = require('mongodb').ObjectID;

const Student = require('../data/Student.js');
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
  const searchId = req.query.id;

  Student.findOne( { id: searchId }, (err, student) => {
    if (err) {
      res.type('html').status(500);
      res.send('Error: ' + err);
    } else if (!student) {
      res.type('html').status(200);
      res.send('No student with the id ' + searchId);
    } else {
      Report.find( { studentId : searchId }, (err, reports) => {
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
    
const saveStudent = function(req, res) {
  const name = req.body.name;
  const age = req.body.age;
  const gender = req.body.gender;
  const school = req.body.school;
  const id = ObjectId();

  const newStudent = new Student({
    id: id,
    name: name,
    age: age,
    gender: gender,
    school: school
  });

  newStudent.save( (err) => {
    if (err) {
      res.send('Error: ' + err);
    } else {
      res.redirect('/student?id=' + id);
    }
  });
};

const routes = {
  get_students: getStudents,
  get_student: getStudent,
  save_student: saveStudent,
};

module.exports = routes;