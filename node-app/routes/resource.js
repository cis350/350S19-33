/* Routes for events */

const ObjectId = require('mongodb').ObjectID;
const Resource = require('../data/Resource.js');

const saveResource = function(req, res) {
  const name = req.query.name;
  const description = req.query.description;
  const id = ObjectId();
  console.log("name: " + name);
  console.log("description: " + description);

  const newResource = new Resource({
    id: id,
    name: name,
    description: description,
  });

  newResource.save( (err) => {
    if (err) {
      res.send({"result": err});
    } else {
      res.send({"result": "resource submitted"});
    }
  });
};

const saveResourceNode = function(req, res) {
  const name = req.body.name;
  const description = req.body.description;
  const id = ObjectId();

  const newResource = new Resource({
    id: id,
    name: name,
    description: description,
  });

  newResource.save( (err) => {
    if (err) {
      res.send('Error: ' + err);
    } else {
      res.redirect('/events');
    }
  });
};

const getResources = function(req, res) {
    Resource.find((err, resources) => {
        if (err) {
          res.send({"result": "error"});
        }
        else {
          res.send({"result": resources});
        }
    });
  };

const routes = {
  save_resource: saveResource,
  save_resource_node: saveResourceNode,
  get_resources: getResources
};

module.exports = routes;