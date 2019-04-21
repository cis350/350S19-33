var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Comment = mongoose.Schema;

var commentSchema = new Comment({
    reportId: {type: String, required: true},
	content: {type: String, required: true},
	adminCommenting: {type: String, required: true},
	studentCommenting: {type: String, required: true},
	date: {type: Date, required: true},
	role: {type: String, required: true}
});

module.exports = mongoose.model('Comment', commentSchema);