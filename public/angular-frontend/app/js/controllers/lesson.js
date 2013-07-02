'use strict';

angular.module('pcs').controller('LessonListCtrl', ['$scope', 'LessonService',

    function ($scope, LessonService) {

        console.log('lessons at LessonListCtrl entry: ' + JSON.stringify($scope.lessons));

        $scope.editLesson = function (lessonToEdit, index) {
            $scope.setActiveLesson(lessonToEdit, index);
            $scope.resetLessonForm();
            $scope.setLeftViewLessonForm('Lektion editieren');
        }

        $scope.newLesson = function () {
            $scope.setActiveLesson({}, $scope.lessons.length);
            $scope.resetLessonForm();
            $scope.setLeftViewLessonForm('Neue Lektion');
        }

        $scope.deleteLesson = function (lessonToDelete, index) {
            LessonService.delete(lessonToDelete);
            $scope.lessons.splice(index, 1);
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

            var saveLessonWithId = function (id) {

                var lesson = {
                    id: id,
                    teacherId: $scope.user.id,
                    start: lessonFormToSave.start,
                    end: lessonFormToSave.end,
                    studentIds: [lessonFormToSave.students.id]
                }
                LessonService.save(lesson);
                $scope.lessons[$scope.activeLessonIndex] = lesson;
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

    }]);