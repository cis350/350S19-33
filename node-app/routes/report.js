/* Routes for statuses */

const Report = require('../data/Report.js');
const Memo = require('../data/Memo.js')
const ObjectId = require('mongodb').ObjectID;



    const getReports = function(req, res) {
                          const person = req.session.user;
                          Report.find((err, reports) => {
                            if (err) {
                              res.type('html').status(500);
                              res.send('Error: ' + err);
                            } else if (reports.length == 0) {
                              res.type('html').status(200);
                              res.send('There are no reports');
                            } else {
                              res.render('reports.ejs', { reports: reports, person: req.session.user });
                            }
                        });
                        };

                        const getReport = function(req, res){
                            const id = req.query._id;

                            Report.findOne( { id: id}, (err, report) => {
                                if (err) {
                                  res.type('html').status(500);
                                  res.send('Error: ' + err);
                                } else if (!report) {
                                  res.type('html').status(200);
                                  res.send('No report with the id ' + id);
                                } else {
                                  res.render('report', { report: report });
                                }
                              });
                            };

                            const editMemo = function(req, res) {
                              const searchId = req.query._id;

                              Memo.findOne( { id: searchId }, (err, memo) => {
                                if (err) {
                                  res.type('html').status(500);
                                  res.send('Error: ' + err);
                                } else if (!memo) {
                                  res.type('html').status(200);
                                  res.send('No memo with the id ' + searchId);
                                } else {
                                  res.render('editMemo', { memo: memo });
                                }
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

     const saveMemo = function(req, res) {
       const name = req.body.name;
       const school = req.body.location;
       const date = req.body.time;
       const description = req.body.date;
       const solution = req.body.host;
       const id = ObjectId();
       //const reportId = req.query._id;

       const newMemo = new Memo({
         id: id,
         //reportId: reportId
         name: name,
         school: school,
         date: date,
         description : description,
         host: host,
       });

       newMemo.save( (err) => {
         if (err) {
           res.send('Error: ' + err);
         } else {
           res.redirect('/report?id=' + id);
         }
       });
     };


                        const getRead = function(req, res) {
                          const person = req.session.user;
                          Report.find((err, reports) => {
                            if (err) {
                              res.type('html').status(500);
                              res.send('Error: ' + err);
                            } else if (reports.length == 0) {
                              res.type('html').status(200);
                              res.send('There are no reports');
                            } else {
                              res.render('reports.ejs', { reports: reports, person: req.session.user });
                            }
                        });
                        };

                        const getUnread = function(req, res) {
                          const person = req.session.user;
                          Report.find((err, reports) => {
                            if (err) {
                              res.type('html').status(500);
                              res.send('Error: ' + err);
                            } else if (reports.length == 0) {
                              res.type('html').status(200);
                              res.send('There are no reports');
                            } else {
                              res.render('reports.ejs', { reports: reports, person: req.session.user });
                            }
                        });
                        };

                        const routes = {
                          get_reports: getReports,
                          get_report: getReport,
                          get_read: getRead,
                          edit_memo: editMemo,
                          update_memo:updateMemo,
                          save_memo: saveMemo,
                          get_unread: getUnread
                        };

                        module.exports = routes;
