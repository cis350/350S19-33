var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Schema = mongoose.Schema;

var memoSchema = new Schema({
    id: {type: String, required: true},
    reportId: {type: String, required:true},
	name: {type: String, required: true},
	school: {type: Date, required: true},
	date: {type: String, required: true},
	description: {type:String, required: true},
	solution: {type: String, required: true}
});

module.exports = mongoose.model('Memo', memoSchema);