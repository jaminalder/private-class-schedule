'use strict';

angular.module('pcs').controller('LessonCtrl', ['$scope', '$resource', '$filter', 'UUIDService','LessonService', 'UserService', 'lessons',

function($scope, $resource, $filter, UUIDService, LessonService, UserService, lessons) {

    console.log('lessons at LessonCtrl entry: ' + JSON.stringify(lessons));

    $scope.lessons = lessons;

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
        var lesson = $filter('date')(angular.copy($scope.lessonForm));
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