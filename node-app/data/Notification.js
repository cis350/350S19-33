var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Notification = mongoose.Schema;

var notificationSchema = new Notification({
	username: {type: String, required: true},
    reportId: {type: String, required: true},
	content: {type: String, required: true},
	date: {type: Date, required: true}
});

module.exports = mongoose.model('Notification', notificationSchema);