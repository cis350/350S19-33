/* Routes for reports */

const Report = require('../data/Report.js');
const Status = require('../data/Status.js');
const Comment = require('../data/Comment.js');
const AdminUser = require('../data/AdminUser.js');
const Memo = require('../data/Memo.js');
const Notification = require('../data/Notification.js');
const ObjectId = require('mongodb').ObjectID;

const getReports = function(req, res) {
    const person = req.session.user;
    
    Report.find((err, reports) => {
        if(!req.session.user){
            res.redirect('login/');
        } else if (err) { 
            res.type('html').status(500);
            res.send('Error: ' + err); 
        } else if (reports.length == 0) {
            res.type('html').status(200);
            res.send('There are no reports');
        } else {
            var finalReports = [];
            reports.forEach(async (item) => {
                //if the adminEmail is null, it means it hasn't been assigned yet
                //assign it while checking for vacation
                if(!item.adminEmail || item.adminEmail == ""){
                    Status.find((err, admins) => {
                        if (err) { 
                            res.type('html').status(500);
                            res.send('Error: ' + err); 
                        } else if (admins.length == 0) {
                            res.type('html').status(200);
                            res.send('There are no reports');
                        } else {
                            var whichAdmin = Math.floor(Math.random() * admins.length);
                            var addAdmin = admins[whichAdmin];
                            while(addAdmin.onVacation){
                                var whichAdmin = Math.floor(Math.random() * admins.length);
                                var addAdmin = admins[whichAdmin];
                            }
                            item.adminEmail = addAdmin.email;
                            item.save((err) => {
                                if(err){
                                    res.type('html').status(500);
                                    res.send('Error: ' + err); 
                                }
                            });
                        }
                    })
                } else {
                        //otherwise, push into the list of reports to display
                        if(item.adminEmail == person.email){
                            finalReports.push(item);
                        }
                    }
            });
            res.render('reports.ejs', { reports: finalReports, person: req.session.user });
        }
    });
};

const getReport = function(req, res){
    const id = req.query.id;
    
    Report.findOne({ id: id}, (err, report) => {
        if (err) {
            res.type('html').status(500);
            res.send('Error: ' + err);
        } else if (!report) {
            res.type('html').status(200);
            res.send('No report with the id ' + id);
        } else {
            //the moment you clicked into the report, you read it
            if(!report.read){
                report.read = true;
                report.save((err) => {
                    if(err){
                        res.type('html').status(500);
                        res.send('Error: ' + err);
                    } else {
                        Comment.find((err, comments) => {
                            if (err) {
                                res.send('Error' + err);
                            } else {
                                AdminUser.find((err, admins) => {
                                    if(err){
                                        res.send("Error" + err);
                                    } else {
                                        res.render('report.ejs', { admins : admins, comments: comments, report: report });
                                    }
                                });
                            }
                            
                        });
                    }
                });
            } else {
                Comment.find((err, comments) => {
                    if (err) {
                        res.send('Error' + err);
                    } else {
                        AdminUser.find((err, admins) => {
                            if(err){
                                res.send("Error" + err);
                            } else {
                                res.render('report.ejs', { admins : admins, comments: comments, report: report });
                            }});
                    }
                });
            }
        }
    });
};

const closeReport = function(req, res){
    const id = req.query.id;
    const person = req.session.user;
    
    Report.findOne({id:id}, (err, report) => {
        if(err){
            res.type('html').status(500);
            res.send('Error: ' + err);
        } else if(!report){
            res.type('html').status(200);
            res.send('No report with the id ' + id);
        } else if (!report.adminCommented){
            res.send("You need to respond to the report before closing");
        } else {
            report.closed = true;
            report.closedDate = Date.now();
            report.save((err) => {
                if(err){
                    res.type('html').status(500);
                    res.send('Error: ' + err);
                } else {
                    Report.find((err, reports) => {
                        if(!req.session.user){
                            res.redirect('login/');
                        } else if (err) {
                            res.type('html').status(500);
                            res.send('Error: ' + err);
                        } else if (reports.length == 0) {
                            res.type('html').status(200);
                            res.send('There are no reports');
                        } else {
                            var finalReports = [];
                            reports.forEach(async (item) => {
                                if(item.adminEmail == person.email){
                                    finalReports.push(item);
                                }
                            });
                            // generate notification for student
                            const newNotif = new Notification({
                                username: report.studentUsername,
                                reportId: report.id,
                                content: "Report " + report.subject + " was closed",
                                date: Date.now(),
                            });
                            
                            newNotif.save( (err) => { 
                                if (err) {
                                    res.type('html').status(500);
                                    res.send('Error: ' + err);
                                }
                                else {
                                    res.render('reports.ejs', { reports: finalReports, person: req.session.user });
                                }
                            });
                        }
                    });
                }
            });
        }
    });
}




const addComment = function(req, res){
    const id = req.query.id;
    const comment = req.body.comment;
    const person = req.session.user;
    const role = req.query.role;
    
    Report.findOne( {id: id}, (err, report) => {
        if (err) {
            res.send('Error' + err);
        }
        else if (!report) {
            res.type('html').status(200);
            res.send('No report with the id ' + id);
        }
        else {
            report.adminCommented = true;
            report.save((err) => { //save the commented field as true
                if(err){
                    res.type('html').status(500);
                    res.send('Error: ' + err);
                } else {
                    var newComment = new Comment({
                        reportId: id,
                        content: comment,
                        user: person.name,
                        role: role,
                        date: Date.now()
                    });
                    newComment.save((err) => {
                        if(err) {
                            res.type('html').status(500);
                            res.send('Error: ' + err);
                        } else {
                            Comment.find((err, comments) => {
                                if (err) {
                                    res.send('Error' + err);
                                } 
                                else {
                                    // generate notification for student
                                    const newNotif = new Notification({
                                        username: report.studentUsername,
                                        reportId: report.id,
                                        content: "Report " + report.subject + " was commented on",
                                        date: Date.now(),
                                    });
                                    
                                    newNotif.save( (err) => { 
                                        if (err) {
                                            res.type('html').status(500);
                                            res.send('Error: ' + err);
                                        }
                                        else {
                                            AdminUser.find((err, admins) => {
                                                if(err){
                                                    res.send("Error" + err);
                                                } else {
                                                    res.render('report.ejs', { admins : admins, comments: comments, report: report });
                                                }
                                            });
                                        }});
                                }
                            });
                        }
                    });
                }
            });
        }
    });
}


const addCommentAndroid = function(req, res){
    const id = req.query.reportId;
    const comment = req.query.content;
    const person = req.query.userName;
    const role = "student";

    Report.findOne( {id: id}, (err, report) => {
    console.log("id" + id);
        if (err) {
            res.send('Error' + err);
        }
        else if (!report) {
            res.type('html').status(200);
            res.send('No report with the id ' + id);
        }
        else {
            var newComment = new Comment({
                reportId: id,
                content: comment,
                user: report.studentUsername,
                role: "student",
                date: Date.now()
            });
            newComment.save((err) => {
                if(err) {
                    res.type('html').status(500);
                    res.send('Error: ' + err);
                } else {
                    Comment.find((err, comments) => {
                        if (err) {
                            res.send('Error' + err);
                        } 
                        else {
                            // generate notification for student
                            const newNotif = new Notification({
                                username: report.studentUsername,
                                reportId: report.id,
                                content: "Report " + report.subject + " was commented on",
                                date: Date.now(),
                            });
                            
                            newNotif.save((err) => { 
                                if (err) {
                                    res.type('html').status(500);
                                    res.send('Error: ' + err);
                                }
                                else {
                                    AdminUser.find((err, admins) => {
                                        if(err){
                                            res.send("Error" + err);
                                        } else {
                                            res.render('report.ejs', { admins : admins, comments: comments, report: report });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    });
}

const updateMemo = function(req,res){
    const searchId = req.body._id;
    const name = req.body.name;
    const school = req.body.school;
    const date = req.body.date;
    const description = req.body.description;
    const solution = req.body.solution;
    
    Memo.findOne( { id: searchId }, (err, memo) => {
        if (err) {
            res.type('html').status(500);
            res.send('Error: ' + err);
        } else if (!memo) {
            res.type('html').status(200);
            res.send('No memo with the id ' + searchId);
        } else {
            memo.name = name;
            memo.school = school;
            memo.date = date;
            memo.description = description;
            memo.solution = solution;
            memo.save();
            res.redirect('/report?id=' + searchId);
        }
    });
}


const showMemos = function(req, res){
    const person = req.session.user;
    Memo.find((err, memos) => {
        if(err) {
            res.type('html').status(500);
            res.send('Error: ' + err);
        } else if(memos.length ==0){
            res.type('html').status(200);
            res.send('There are no memos');
        }else{
            res.render('memos.ejs', {memos:memos, person:req.session.user});
        }
    });
    
};


const getRead = function(req, res) {
    const person = req.session.user;
    Report.find((err, reports) => {
        if(!req.session.user){
            res.redirect('login/');
        } else if (err) {
            res.type('html').status(500);
            res.send('Error: ' + err);
        } else if (reports.length == 0) {
            res.type('html').status(200);
            res.send('There are no reports');
        } else {
            var finalReports = [];
            reports.forEach(async (item) => {
                if(item.adminEmail == person.email && item.read){
                    finalReports.push(item);
                }
            });
            res.render('reports.ejs', { reports: finalReports, person: req.session.user });
        }
    });
};

const getClosed = function(req, res) {
    const person = req.session.user;
    Report.find((err, reports) => {
        if(!req.session.user){
            res.redirect('login/');
        } else if (err) {
            res.type('html').status(500);
            res.send('Error: ' + err);
        } else if (reports.length == 0) {
            res.type('html').status(200);
            res.send('There are no reports');
        } else {
            var finalReports = [];
            reports.forEach(async (item) => {
                if(item.adminEmail == person.email && item.closed){
                    finalReports.push(item);
                }
            });
            res.render('reports.ejs', { reports: finalReports, person: req.session.user });
        }
    });
};

const getUnread = function(req, res) {
    const person = req.session.user;
    Report.find((err, reports) => {
        if(!req.session.user){
            res.redirect('login/');
        } else if (err) {
            res.type('html').status(500);
            res.send('Error: ' + err);
        } else if (reports.length == 0) {
            res.type('html').status(200);
            res.send('There are no reports');
        } else {
            var finalReports = [];
            reports.forEach(async (item) => {
                if(item.adminEmail == person.email && !item.read){
                    finalReports.push(item);
                }
            });
            res.render('reports.ejs', { reports: finalReports, person: req.session.user });
        }
    });
};

const getPending = function(req, res) {
    const person = req.session.user;
    Report.find((err, reports) => {
        if(!req.session.user){
            res.redirect('login/');
        } else if (err) {
            res.type('html').status(500);
            res.send('Error: ' + err);
        } else if (reports.length == 0) {
            res.type('html').status(200);
            res.send('There are no reports');
        } else {
            var finalReports = [];
            reports.forEach(async (item) => {
                if(item.adminEmail == person.email && !item.adminCommented){
                    finalReports.push(item);
                }
            });
            res.render('reports.ejs', { reports: finalReports, person: req.session.user });
        }
    });
};

const saveMemo = function(req, res) {
    const name = req.body.name;
    const school = req.body.school;
    const date = req.body.date;
    const description = req.body.description;
    const solution = req.body.solution;
    const id = ObjectId();
    const reportId = req.body.id;
    
    const newMemo = new Memo({
        id: id,
            //reportId: reportId
        reportId: reportId,
        name: name,
        school: school,
        date: date,
        reportDescription : description,
        solution: solution
            
    });
    
    newMemo.save( (err) => {
        if (err) {
            res.send('Error: ' + err);
        } else {
            newMemo.save((err) => {
                if(err){
                    res.redirect('dashboard/');
                } else{
                    req.session.memo = newMemo;
                    res.redirect('dashboard/');
                }
                
            });
        }
    });
};

const deleteMemo = function(req,res){
    const id = req.query.id;
    
    const queryObject = { "id" : id };
    
    Memo.deleteOne({ "id" : id }, function (err) {
        if (err) {
            return handleError(err);
        } else {
            Memo.find( (err, memos) => {
                if (err) {
                    res.type('html').status(500);
                    res.send('Error: ' + err);
                } else if (memos.length == 0) {
                    res.type('html').status(200);
                    res.send('There are no memos');
                } else {
                    res.render('memos', { memos: memos });
                }
            });
        }
    });
};

const editMemo = function(req, res) {
    const id = req.query.id;
    
    Memo.findOne({ "id" : id }, (err, memo) => {
        if (err) {
            res.type('html').status(500);
            res.send('Error: ' + err);
        } else if (!memo) {
            res.type('html').status(200);
            res.send('No memo with the id ' + id);
        } else {
            res.render('editMemo', { memo: memo });
        }
    });
};

const metricCount = function(req, res) {
    const searchUsername = req.query.username;
    Report.find( { studentUsername : searchUsername }, (err, reports) => {
        if (err) {
            res.send({'result' : "error"});
        } else if (!reports) {
            res.send({'result' : "0"});
        } else {
            var numPending = 0;
            var numClosed = 0;
            var numNoAction = 0;
            for (var i = 0; i < reports.length; i++) {
                const report = reports[i].toObject();
                if (report.closed) {
                    numClosed++;
                } else if (report.adminCommented) {
                    numPending++;
                } else {
                    numNoAction++;
                }
            }
            res.send({'result': "{'numClosed': " + numClosed + 
                ", 'numNoAction': " + numNoAction + ", 'numPending': " + 
                numPending + "}"});
        }
    });
}

const getComment = function(req, res){
    const reportId = req.query.reportId;

    Comment.find({reportId: reportId}, (err,comments) =>{
        if(err){
            res.send({'result': error});
        }else if(!comments){
            res.send({'result' : "0"});
        } else{
            res.send({'result' : comments});
        }
    });
}


const getStudentReport = function(req, res) {
    const searchName = req.query.username;
    
    Report.find( { studentUsername : searchName }, (err, reports) => {
        if (err) {
            res.send({'result' : error});
        } else if (!reports) {
            res.send({'result' : "0"});
        } else {
            res.send({'result': reports});
        }
    });
}

const getNotifications = function(req, res) {
    const username = req.query.username;
    
    const queryObject = { "username" : username };
    
    Notification.find(queryObject, (err, notifs) => {
        if (err) {
            res.send({"result": "error"});
        }
        else {
            res.send({"result": notifs});
        }
    });
};


const saveStudentReport = function(req, res){
    const username = req.query.username;
    const name = req.query.name;
    const date = req.query.date;
    const subject = req.query.subject;
    const description = req.query.description;
    const person = req.query.person;
    const id = ObjectId();
    
    Status.find({ onVacation: false}, (err, admins) => {
        if (err) { 
            res.type('html').status(500);
            res.send('Error: ' + err); 
        } else if (admins.length == 0) {
            res.type('html').status(200);
            res.send('There are no reports');
        } else {
            var whichAdmin = Math.floor(Math.random() * admins.length);
            var addAdmin = admins[whichAdmin];
            var adminEmail = addAdmin.email;
            const newReport = new Report({
                id: id,
                reportDescription: description,
                studentUsername: username,
                studentName: name,
                date: date,
                subject: subject,
                reportForWhom: person,
                adminCommented: false,
                adminEmail: adminEmail,
                closed: false,
                read: false,
                closedDate: date
            });
            
            newReport.save( (err) => {
                if (err) {
                    res.send({"result": err});
                }
                else {
                    console.log("Success!");
                    res.send({"result": "report submitted"});
                }
            });
        }
    });
};

const deleteReport = function(req,res){
    const id = req.query.id;

    const queryObject = { "id" : id };
    
    Report.deleteOne({ "id" : id }, function (err) {
        if (err) {
            return handleError(err);
        } else {
            Report.find( (err, reports) => {
                if (err) {
                    res.type('html').status(500);
                    res.send('Error: ' + err);
                } else if (reports.length == 0) {
                    res.type('html').status(200);
                    res.send('There are no reports');
                } else {
                    res.send({"result": "report deleted"});
                }
            });
        }
    });
};


const editReport = function(req, res) {
    const name = req.query.name;
    const date = req.query.date;
    const subject = req.query.school;
    const description = req.query.age;
    const person = req.query.gender;
    const id = req.query.id;
    
    Report.findOne({ id: id}, (err, report) => {
        if (err) {
            res.send({"result": "error in find: " + err});
        }
        else if (!report) {
            res.send({"result": "no such user"});
        }
        else {
            if(date != "" && date != null && date != undefined){
                report.date = date;
            }
            if(subject != "" && subject != null && subject != undefined){
                report.subject = subject;
            }
            if(description != "" && description != null && description != undefined){
                report.reportDescription = description;
            }
            if(person != "" && person != null && person != person){
                report.reportForWhom = person;
            }
            report.save((err) => {
                if(err){
                    res.send({"result": error});
                } else {
                    res.send({"result": 'edited'});
                }
            });
        }
    });
};

const forwardReport = function(req,res){
    const adminEmail = req.query.admin;
    const id = req.query.id;
    
    const queryObject = { "id" : id };
    
    Report.findOne({ id: id}, (err, report) => {
        if (err) {
            res.type('html').status(500);
            res.send('Error: ' + err);
        } else if (!report) {
            res.type('html').status(200);
            res.send('No report with the id ' + id);
        } else {
            //the moment you clicked into the report, you read it
            report.adminEmail = adminEmail;
            report.save((err) => {
                if(err){
                    res.type('html').status(500);
                    res.send('Error: ' + err); 
                } else {
                    res.redirect("/reports");
                }
            });
        };
    });
}

const routes = {
    get_reports: getReports,
    get_report: getReport,
    get_read: getRead,
    close_report: closeReport,
    edit_memo: editMemo,
    update_memo:updateMemo,
    save_memo: saveMemo,
    show_memos: showMemos,
    delete_memo: deleteMemo,
    add_comment: addComment,
    get_unread: getUnread,
    get_closed: getClosed,
    metric_count: metricCount,
    get_student_report: getStudentReport,
    save_student_report: saveStudentReport,
    edit_report: editReport,
    delete_report: deleteReport,
    add_comment_android: addCommentAndroid,
    forward_report: forwardReport,
    get_pending: getPending,
    get_comment: getComment
};

module.exports = routes;