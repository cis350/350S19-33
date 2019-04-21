var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Report = mongoose.Schema;

var reportSchema = new Report({
    id: {type: String, required: true, unique: false},
    description: {type:String, required:true, unique: false}
    memoId: {type: String, required: false},
	adminEmail: {type: String, required: false},
	date: {type: Date, required: true},
	studentName: {type: String, required: true},
	studentUsername : {type: String, required: false},
	subject: {type: String, required: true},
	reportForWhom: {type: String, required: true},
	read: {type: Boolean, required: false},
	closed: {type: Boolean, required: false},
	comment: {type: String, required: false},
	closedDate: {type: Date, required: false}
});

module.exports = mongoose.model('Report', reportSchema);