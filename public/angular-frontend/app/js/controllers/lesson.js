'use strict';

angular.module('pcs').controller('LessonListCtrl',

    function ($scope, LessonService) {

        $scope.newLesson = function () {
            var lessonStart = new Date();
            var lessonEnd = new Date(lessonStart.getTime() + (30 * 60000)); // start + 30 min
            $scope.setActiveLesson({
                start: lessonStart.getTime(),
                end: lessonEnd.getTime()
            }, $scope.lessons.length);
            $scope.resetLessonForm();
            $scope.setLeftViewLessonForm('Neue Lektion', 'Bitte neue Lektion erfassen');
        }

        $scope.deleteLesson = function (lessonToDelete, index) {
            LessonService.delete(lessonToDelete);
            $scope.lessons.splice(index, 1);
            $scope.fireLessonsChangedEvent();
        }

    });


angular.module('pcs').controller('LessonFormCtrl',

    function($scope, $resource, $filter, UUIDService, LessonService, DateTimeService, AlertService) {


        $scope.hideLessonDetail = function(){
            $scope.setLeftViewDefault();
        }

        $scope.saveLesson = function () {

            var lessonFormToSave = angular.copy($scope.lessonForm);

            var lessonDate = lessonFormToSave.date;
            var startTime = lessonFormToSave.startTime;
            var endTime = lessonFormToSave.endTime;

            var saveLessonWithId = function (id) {

                var lesson = {
                    id: id,
                    teacherId: $scope.user.id,
                    start: DateTimeService.getMillisFromTimeString(lessonDate, startTime),
                    end: DateTimeService.getMillisFromTimeString(lessonDate, endTime),
                    studentIds: []
                }
                var i
                for (i in lessonFormToSave.lessonStudentIds) {
                    var studentId = lessonFormToSave.lessonStudentIds[i];
                    lesson.studentIds.push(
                        {_id: studentId});
                }
                LessonService.save(lesson);
                $scope.lessons[$scope.activeLessonIndex] = lesson;
                $scope.fireLessonsChangedEvent();
                AlertService.addSuccess("Lektion gespeichert");
            }

            if(lessonFormToSave.id === undefined){
                UUIDService.generate(saveLessonWithId);
            } else {
                saveLessonWithId(lessonFormToSave.id);
            }

            $scope.resetLessonForm();
            $scope.hideLessonDetail();
        }

        $scope.cancelLessonForm = function () {
            $scope.resetLessonForm();
            $scope.hideLessonDetail();
        }

        $scope.isUnchanged = function (lessonForm) {
            return angular.equals(lessonForm, $scope.activeLesson);
        };

        $scope.select2Options = {
            placeholder: 'Schüler auswählen'
            // allowClear:true
        };

    });