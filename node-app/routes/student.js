/* Routes for student search */
var session = require('client-sessions');

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
      res.render('student', { student: student });
    }
  });
};

const routes = {
  get_students: getStudents,
  get_student: getStudent
};

module.exports = routes;