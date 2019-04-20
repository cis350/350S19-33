var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Resource = mongoose.Schema;

var resourceSchema = new Resource({
    id: {type: String, required: true, unique: true},
	name: {type: String, required: false},
	description: {type: String, required: true}
});

module.exports = mongoose.model('Resource', resourceSchema);