/* Routes for user accounts */
var session = require('client-sessions');

const Notification = require('../data/Notification.js');

const getNotifications = function(req, res) {
  const username = req.query.username;

  const queryObject = { "username" : username };

  Notification.find(queryObject, (err, notifs) => { 
    if (err) {
      res.send({"result": "error"});
    }
    else {
      res.send({"result": notifs});
    }
  }); 
};

const routes = {
  get_notifs: getNotifications,
};

module.exports = routes;
