/* Routes for reports */

const Report = require('../data/Report.js');
const Comment = require('../data/Comment.js');
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
                  item.adminEmail = addAdmin.email;
                  item.save((err) => {
                    if(err){
                      res.type('html').status(500);
                      res.send('Error: ' + err); 
                    }
                  })
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
          res.render('report.ejs', { comments: comments, report: report });
        }
      });
            }
            });
          } else {
          Comment.find((err, comments) => {
          if (err) {
            res.send('Error' + err);
          } else {
          res.render('report.ejs', { comments: comments, report: report });
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
        } else if (report.comment == "" || !report.comment){
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

  Report.findOne( {id: id}, (err, report) => {
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
           adminCommenting: person.email,
           studentCommenting: report.studentUsername,
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
          res.render('report.ejs', { comments: comments, report: report });
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
        description : description,
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
    get_closed: getClosed
};

module.exports = routes;