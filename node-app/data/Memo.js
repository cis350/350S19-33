var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Schema = mongoose.Schema;

var memoSchema = new Schema({
    id: {type: String, required: true},
    reportId: {type: String, required:false},
	name: {type: String, required: false},
	school: {type: String, required: false},
	date: {type: Date, required: true},
	description: {type:String, required: false},
	solution: {type: String, required: false}
});

module.exports = mongoose.model('Memo', memoSchema);