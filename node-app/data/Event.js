var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Schema = mongoose.Schema;

var eventSchema = new Schema({
	id: {type: String, required: true, unique: true},
	name: {type: String, required: true, unique: true},
	location: {type: String, required: true},
	time: {type: String, required: true},
	host: {type: String, required: true},
	description: {type: String, required: true},
	students: {type: [String], required: true},
	comments: {type: [String], required: true}
    });

module.exports = mongoose.model('Event', eventSchema);
