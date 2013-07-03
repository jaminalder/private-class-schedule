'use strict';

angular.module('pcs').controller('LessonListCtrl', ['$scope', 'LessonService',

    function ($scope, LessonService) {

        console.log('lessons at LessonListCtrl entry: ' + JSON.stringify($scope.lessons));


        $scope.newLesson = function () {
            $scope.setActiveLesson({}, $scope.lessons.length);
            $scope.resetLessonForm();
            $scope.setLeftViewLessonForm('Neue Lektion');
        }

        $scope.deleteLesson = function (lessonToDelete, index) {
            LessonService.delete(lessonToDelete);
            $scope.lessons.splice(index, 1);
            $scope.fireLessonsChangedEvent();
        }

    }]);


angular.module('pcs').controller('LessonFormCtrl', ['$scope', '$resource', '$filter', 'UUIDService','LessonService', 'UserService',

    function($scope, $resource, $filter, UUIDService, LessonService) {

        console.log('lessons at LessonFormCtrl entry: ' + JSON.stringify($scope.lessons));
        console.log('students at LessonFormCtrl entry: ' + JSON.stringify($scope.students));

        $scope.hideLessonDetail = function(){
            $scope.setLeftViewDefault();
        }

        $scope.saveLesson = function () {

            var lessonFormToSave = angular.copy($scope.lessonForm);

            console.log('lessonFormToSave: ' + JSON.stringify(lessonFormToSave));

            var saveLessonWithId = function (id) {

                var lesson = {
                    id: id,
                    teacherId: $scope.user.id,
                    start: lessonFormToSave.start,
                    end: lessonFormToSave.end,
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
            }

            if(lessonFormToSave.id === undefined){
                UUIDService.generate(saveLessonWithId);
            } else {
                saveLessonWithId(lessonFormToSave.id);
            }

            $scope.resetLessonForm();
            $scope.hideLessonDetail();
        }

        $scope.isUnchanged = function (lessonForm) {
            return angular.equals(lessonForm, $scope.activeLesson);
        };

        $scope.select2Options = {
            placeholder: 'Schüler auswählen'
 //           allowClear:true
        };

    }]);