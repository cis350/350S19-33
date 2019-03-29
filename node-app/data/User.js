var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Schema = mongoose.Schema;

var userSchema = new Schema({
	email: {type: String, required: true, unique: true},
	password: {type: String, required: true},
	name: {type: String, required: true},
	school: {type: String, required: true}
    });

module.exports = mongoose.model('User', userSchema);
