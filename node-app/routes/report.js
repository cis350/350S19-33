/* Routes for reports */

const Report = require('../data/Report.js');
const Memo = require('../data/Memo.js');
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
          if(item.adminEmail == person.email){
            finalReports.push(item);
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
              console.log(report);
              if(!report.read){
                report.read = true;
                report.save((err) => {
              if(err){
                res.type('html').status(500);
               res.send('Error: ' + err);
             } else {
              res.render('report', { report: report });
            }
            });
          } else {
             res.render('report', { report: report });
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
        }else{
            report.closed = true;
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
                  res.render('reports.ejs', { reports: finalReports, person: req.session.user });
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
          report.comment = comment;
          report.save((err) => {
            if(err){
                res.type('html').status(500);
                res.send('Error: ' + err);
            } else {
                req.session.comment = comment;
                res.render('dashboard/');
            }
        });

};
});
};


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
  console.log(person);
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
  console.log(person);
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

