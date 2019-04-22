var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Schema = mongoose.Schema;

var adminUserSchema = new Schema({
	email: {type: String, required: true, unique: true},
	password: {type: String, required: true},
	name: {type: String, required: true},
	school: {type: String, required: true},
	location: {type: String, required: true},
	phone: {type: String, required: true},
	gender: {type: String, required: true},
	role: {type: String, required: true},
	openReports: {type: Array, required: true},
	closedReports: {type: Array, required: true}
    });

module.exports = mongoose.model('AdminUser', adminUserSchema);
