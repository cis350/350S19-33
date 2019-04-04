var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Student = mongoose.Schema;

var studentSchema = new Student({
	username: {type: String, required: true, unique: true},
	name: {type: String, required: true},
	age: {type: Number, required: true},
	gender: {type: String, required: true},
	school: {type: String, required: true}
});

module.exports = mongoose.model('Student', studentSchema);