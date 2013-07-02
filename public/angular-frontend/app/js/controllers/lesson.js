'use strict';

angular.module('pcs').controller('LessonCtrl', ['$scope', '$resource', '$filter', 'UUIDService','LessonService', 'UserService', 'lessons', 'StudentService', 'students',

function($scope, $resource, $filter, UUIDService, LessonService, UserService, lessons, StudentService, students) {

    console.log('lessons at LessonCtrl entry: ' + JSON.stringify(lessons));
    console.log('students at LessonCtrl entry: ' + JSON.stringify(students));

    $scope.lessons = lessons;
    $scope.students = students;

    $scope.showLessonDetail = function(title){
        $scope.lessonDetailTitle = title;
        $scope.lessonDetailVisible = true;
    }

    $scope.hideLessonDetail = function(){
        $scope.lessonDetailVisible = false;
    }

    $scope.hideLessonDetail();

    $scope.getEmptyLesson = function (callback) {

        var constructEmptyLesson = function (id) {
            var emptyLesson = {};
            emptyLesson.id = id;
            emptyLesson.teacherId = $scope.user.id;
            emptyLesson.studentIds = [] ;
            callback(emptyLesson);
        }

        UUIDService.generate(constructEmptyLesson);

    };

    $scope.resetLessonForm = function () {
        $scope.lessonForm = angular.copy($scope.activeLesson);
    };
//    (date[, format]   ('date', 'format:dd.MM.YYYY HH:mm')

    $scope.saveLesson = function () {
        console.log('lessonForm: ' + JSON.stringify($scope.lessonForm));
        var lesson = {};
        lesson.id = $scope.lessonForm.id;
        lesson.start = (angular.copy($scope.lessonForm.start));
        lesson.end = (angular.copy($scope.lessonForm.end));
        lesson.teacherId = $scope.lessonForm.teacherId;
        lesson.studentIds = [(angular.copy($scope.lessonForm.students.id))];
        console.log('lesson to save: ' + JSON.stringify(lesson));
        LessonService.save(lesson);
        $scope.lessons[$scope.activeLessonIndex] = lesson;
        $scope.resetLessonForm();
        $scope.hideLessonDetail();
    };

    $scope.editLesson = function(lessonToEdit, index) {
        console.log('lessonToEdit: ' + JSON.stringify(lessonToEdit));
        $scope.activeLesson = lessonToEdit;
        $scope.activeLessonIndex = index;
        $scope.resetLessonForm();
        $scope.showLessonDetail('Lektion editieren');
        console.log('activeLesson: ' + JSON.stringify($scope.activeLesson));

    }

    $scope.newLesson = function() {
        $scope.getEmptyLesson(function (emptyLesson) {
            $scope.activeLesson = emptyLesson;
            $scope.activeLessonIndex = $scope.lessons.length;
            $scope.students.id = emptyLesson.studentIds;
            $scope.resetLessonForm();
            $scope.showLessonDetail('Neue Lektion');
        });
    }

    $scope.deleteLesson = function(lessonToDelete, index) {
        LessonService.delete(lessonToDelete);
        $scope.lessons.splice(index, 1);
    }

    $scope.isUnchanged = function (lessonForm) {
        return angular.equals(lessonForm, $scope.activeLesson);
    };

}]);

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