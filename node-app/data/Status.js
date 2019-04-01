var mongoose = require('mongoose');

mongoose.connect('mongodb+srv://dorothy:dorothy@cluster0-8qbcz.mongodb.net/test?retryWrites=true');

var Schema = mongoose.Schema;

var statusSchema = new Schema({
	email: {type: String, required: true, unique: true},
	name: {type: String, required: true},
	onVacation: {type: Boolean, required: true}
    });

module.exports = mongoose.model('Status', statusSchema);