var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Schema = mongoose.Schema;

var studentUserSchema = new Schema({
	username: {type: String, required: true, unique: true},
	password: {type: String, required: true},
	school: {type: String, required: true},
	age: {type: String, required: true},
	gender: {type: String, required: true}
    });

module.exports = mongoose.model('StudentUser', studentUserSchema);
