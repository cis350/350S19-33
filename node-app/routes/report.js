/* Routes for statuses */

const Report = require('../data/Report.js');
const ObjectId = require('mongodb').ObjectID;

const getReports = function(req, res) {
  const person = req.session.user;
  Report.find((err, reports) => {
    if (err) { 
      res.type('html').status(500);
      res.send('Error: ' + err); 
    } else if (reports.length == 0) {
      res.type('html').status(200);
      res.send('There are no reports');
    } else {
      res.render('reports.ejs', { reports: reports, person: req.session.user });
    }
});
};

const getReport = function(req, res){
    const id = req.query._id;

    Report.findOne( { id: id}, (err, report) => {
        if (err) {
          res.type('html').status(500);
          res.send('Error: ' + err);
        } else if (!report) {
          res.type('html').status(200);
          res.send('No report with the id ' + id);
        } else {
          res.render('report', { report: report });
        }
      });
    };

const getRead = function(req, res) {
  const person = req.session.user;
  Report.find((err, reports) => {
    if (err) { 
      res.type('html').status(500);
      res.send('Error: ' + err); 
    } else if (reports.length == 0) {
      res.type('html').status(200);
      res.send('There are no reports');
    } else {
      res.render('reports.ejs', { reports: reports, person: req.session.user });
    }
});
};

const getUnread = function(req, res) {
  const person = req.session.user;
  Report.find((err, reports) => {
    if (err) { 
      res.type('html').status(500);
      res.send('Error: ' + err); 
    } else if (reports.length == 0) {
      res.type('html').status(200);
      res.send('There are no reports');
    } else {
      res.render('reports.ejs', { reports: reports, person: req.session.user });
    }
});
};

const routes = {
  get_reports: getReports,
  get_report: getReport,
  get_read: getRead,  
  get_unread: getUnread
};

module.exports = routes;