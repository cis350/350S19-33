/* Routes for events */

const ObjectId = require('mongodb').ObjectID;
const Event = require('../data/Event.js');

const showEvents = function(req, res) {
  if (!req.session.user) {
    res.redirect('login/');
    return;
  }

  Event.find( (err, allEvents) => {
     if (err) {
        res.type('html').status(500);
        res.send('Error: ' + err);
     }
     else if (allEvents.length == 0) {
        res.type('html').status(200);
        res.send('There are no events');
     } else {
        res.render('events', { events: allEvents });
     }
  });
};

const showEvent = function(req, res) {
  const searchId = req.query.id;

  Event.findOne( { id: searchId }, (err, event) => {
    if (err) {
      res.type('html').status(500);
      res.send('Error: ' + err);
    } else if (!event) {
      res.type('html').status(200);
      res.send('No event with the id ' + searchId);
    } else {
      res.render('event', { event: event });
    }
  });
};

const editEvent = function(req, res) {
  const searchId = req.query.id;

  Event.findOne( { id: searchId }, (err, event) => {
    if (err) {
      res.type('html').status(500);
      res.send('Error: ' + err);
    } else if (!event) {
      res.type('html').status(200);
      res.send('No event with the id ' + searchId);
    } else {
      res.render('editEvent', { event: event });
    }
  });
};

const updateEvent = function(req, res) {
  const searchId = req.body.id;
  const name = req.body.name;
  const location = req.body.location;
  const time = req.body.time;
  const date = req.body.date;
  const host = req.body.host;
  const description = req.body.description;

  Event.findOne( { id: searchId }, (err, event) => {
    if (err) {
      res.type('html').status(500);
      res.send('Error: ' + err);
    } else if (!event) {
      res.type('html').status(200);
      res.send('No event with the id ' + searchId);
    } else {
      event.name = name;
      event.location = location;
      event.time = time;
      event.date = date;
      event.host = host;
      event.description = description;
      event.save();
      res.redirect('/event?id=' + searchId);
    }
  });

}

const saveEvent = function(req, res) {
  const name = req.body.name;
  const location = req.body.location;
  const time = req.body.time;
  const date = req.body.date;
  const host = req.body.host;
  const description = req.body.description;
  const id = ObjectId();

  const newEvent = new Event({
    id: id,
    name: name,
    location: location,
    time: time,
    date: date,
    host: host,
    description: description,
  });

  newEvent.save( (err) => {
    if (err) {
      res.send('Error: ' + err);
    } else {
      res.redirect('/event?id=' + id);
    }
  });
};

const deleteEvent = function(req, res) {
  const id = req.query.id;

  const queryObject = { "id" : id };

  Event.deleteOne({ "id" : id }, function (err) {
    if (err) {
      return handleError(err);
    } else {
        Event.find( (err, allEvents) => {
          if (err) {
            res.type('html').status(500);
            res.send('Error: ' + err);
          } else if (allEvents.length == 0) {
            res.type('html').status(200);
            res.send('There are no events');
          } else {
            res.render('events', { events: allEvents });
          }
        });
    }
  });
};

const getEvents = function(req, res) {
  Event.find((err, events) => {
    if (err) {
      res.send({"result": "error"});
    } else {
      res.send({"result": events});
    }
  });
};

const registerStudent = function(req, res) {
  const eventID = req.query.id;
  const studentUsername = req.query.username;

  Event.findOne( { id: eventID }, (err, event) => {
    if (err) {
      res.type('html').status(500);
      res.send('Error: ' + err);
    } else if (!event) {
      res.type('html').status(200);
      res.send('No event with the id ' + eventID);
    } else {
      let isRegistered = false;
      for (let i = 0; i < event.students.length; i++) {
        if (event.students[i] == studentUsername) {
          isRegistered = true;
        }
      }
      let update = { $push: { students: studentUsername } };
      if (isRegistered) {
        update = { $pull: { students: studentUsername } };
        console.log("is registered, update set to pull");
      }
      var query = { id: eventID };
      var options = {new: true};
      Event.findOneAndUpdate(query, update, options, function(err, event) {
        if (err) {
          res.type('html').status(500);
          res.send('Error: ' + err);
        } else if (!event) {
          res.type('html').status(200);
          res.send('No event with the id ' + eventID);
        } else {
          Event.find((err, events) => {
            if (err) {
              res.send({"result": "error"});
            } else {
              res.send({"result": events});
            }
          });
        }
      })
    }
  });
};  

const addComment = function(req, res) {
  const eventID = req.query.id;
  const comment = req.query.comment;

  var query = { id: eventID };
  var update = { $push: { comments: comment } };
  var options = {new: true};
  Event.findOneAndUpdate(query, update, options, function(err, event) {
    if (err) {
      res.type('html').status(500);
      res.send('Error: ' + err);
    } else if (!event) {
      res.type('html').status(200);
      res.send('No event with the id ' + eventID);
    } else {
      res.send({"result": "success"});
    }
  });
};  

const routes = {
  show_events: showEvents,
  show_event: showEvent,
  save_event: saveEvent,
  delete_event: deleteEvent,
  edit_event: editEvent,
  update_event: updateEvent,
  get_events: getEvents,
  register_student: registerStudent,
  add_comment: addComment
};

module.exports = routes;