/* Routes for events */

const ObjectId = require('mongodb').ObjectID;
const Event = require('../data/Event.js');

const showEvents = function(req, res) {
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
  const host = req.body.host;
  const description = req.body.description;
  const id = ObjectId();

  const newEvent = new Event({
    id: id,
    name: name,
    location: location,
    time: time,
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

const routes = {
  show_events: showEvents,
  show_event: showEvent,
  save_event: saveEvent,
  delete_event: deleteEvent,
  edit_event: editEvent,
  update_event: updateEvent
};

module.exports = routes;