/* Routes for statuses */

const Status = require('../data/Status.js');

const changeStatus = function(req, res) {
  const person = req.session.user;
  Status.findOne({ email: person.email }, (err, daStatus) => {
    if (err) { 
      res.type('html').status(500);
      res.send('Error: ' + err); 
    } else if (!daStatus) {
      res.type('html').status(200);
      res.send('No person for email ' + person.email); 
    } else {
      daStatus.onVacation = !daStatus.onVacation;
      daStatus.save((err) => {
      if (err) {
        res.type('html').status(500);
        res.send('Error: ' + err);
      } else {
          Status.find( (err, allStatuses) => {
     if (err) {
        res.type('html').status(500);
        res.send('Error: ' + err);
     }
     else if (allStatuses.length == 0) {
        res.type('html').status(200);
        res.send('There are no people');
     }
else {
        res.render('status.ejs', { statuses: allStatuses, person: req.session.user });
     }
});
      }
    });
    }
  });
}

const getStatuses = function(req, res) {
  Status.find( (err, allStatuses) => {
     if (err) {
        res.type('html').status(500);
        res.send('Error: ' + err);
     }
     else if (allStatuses.length == 0) {
        res.type('html').status(200);
        res.send('There are no people');
     }
else {
        res.render('status.ejs', { statuses: allStatuses, person: req.session.user });
     }
});
}

const routes = {
  get_statuses: getStatuses,
  change_status: changeStatus,
};

module.exports = routes;
