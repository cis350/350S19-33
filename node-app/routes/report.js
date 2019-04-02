/* Routes for statuses */

const Report = require('../data/Report.js');

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
  get_read: getRead,  
  get_unread: getUnread
};

module.exports = routes;
