var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Report = mongoose.Schema;

var reportSchema = new Report({
	id: {type: String, required: true, unique: true},
	adminEmail: {type: String, required: true},
	date: {type: Date, required: true},
	studentName: {type: String, required: true},
	studentUsername : {type: String, required: true},
	subject: {type: String, required: true},
	reportDescription: {type: String, required: true},
	reportForWhom: {type: String, required: true},
	read: {type: Boolean, required: true}
});

module.exports = mongoose.model('Report', reportSchema);