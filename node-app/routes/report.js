/* Routes for statuses */

const Report = require('../data/Report.js');

const getReports = function(req, res) {
  const person = req.session.user;

  Report.find((err, reports) => {
    if(!req.session.user){
      res.redirect('login/');
    } else if (err) { 
      res.type('html').status(500);
      res.send('Error: ' + err); 
    } else if (reports.length == 0) {
      res.type('html').status(200);
      res.send('There are no reports');
    } else {
      var finalReports = [];
        reports.forEach(async (item) => {
          if(item.adminEmail == person.email){
            finalReports.push(item);
        }
      });
      res.render('reports.ejs', { reports: finalReports, person: req.session.user });
    }
});
};

const getRead = function(req, res) {
  const person = req.session.user;
  console.log(person);
  Report.find((err, reports) => {
    if(!req.session.user){
      res.redirect('login/');
    } else if (err) { 
      res.type('html').status(500);
      res.send('Error: ' + err); 
    } else if (reports.length == 0) {
      res.type('html').status(200);
      res.send('There are no reports');
    } else {
      var finalReports = [];
        reports.forEach(async (item) => {
          if(item.adminEmail == person.email && item.read){
            finalReports.push(item);
        }
      });
      res.render('reports.ejs', { reports: finalReports, person: req.session.user });
    }
});
};

const getUnread = function(req, res) {
  const person = req.session.user;
  Report.find((err, reports) => {
    if(!req.session.user){
      res.redirect('login/');
    } else if (err) { 
      res.type('html').status(500);
      res.send('Error: ' + err); 
    } else if (reports.length == 0) {
      res.type('html').status(200);
      res.send('There are no reports');
    } else {
      var finalReports = [];
        reports.forEach(async (item) => {
          if(item.adminEmail == person.email && !item.read){
            finalReports.push(item);
        }
      });
      res.render('reports.ejs', { reports: finalReports, person: req.session.user });
    }
});
};

const routes = {
  get_reports: getReports,  
  get_read: getRead,  
  get_unread: getUnread
};

module.exports = routes;