'use strict';

var lesson = angular.module('pcs');

lesson.controller('LessonCtrl', ['$scope', '$resource', '$filter', 'UUIDService','LessonService', 'UserService',

function($scope, $resource, $filter, UUIDService, LessonService, UserService) {

    // User should somehow be injected later...
    var User = UserService;

    var Lesson = LessonService;

    User.get(function (userResult) {
        console.log('user result: ' + JSON.stringify(userResult));
        $scope.user = userResult;
        $scope.lessons = Lesson.allLessonsOfTeacher({'teacherId': userResult.id._id});
    });

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
            var emptyLesson = new Lesson();

            emptyLesson.id = id;
            emptyLesson.teacherId = $scope.user.id;
//            emptyLesson.studentIds = [""] ;

            //set the optional fields to "" to ensure a proper conversion to person on the server
            //not sure, if this is really a good idea

            callback(emptyLesson);
        }

        UUIDService.generate(constructEmptyLesson);

    };

    $scope.resetLessonForm = function () {
        $scope.lessonForm = angular.copy($scope.activeLesson);
    };
//    (date[, format]   ('date', 'format:dd.MM.YYYY HH:mm')

    $scope.saveLesson = function () {
       var startDate = $filter('date', 'format:dd.MM.YYYY HH:mm')(angular.copy($scope.lessonForm.start))   ;
        var endDate =  $filter('date', 'format:dd.MM.YYYY HH:mm')(angular.copy($scope.lessonForm.end)  )  ;
        var lesson =  $filter('date', 'format:dd.MM.YYYY HH:mm')(angular.copy($scope.lessonForm));
//        lesson.start = startDate;
//        lesson.end = endDate;

        lesson.$save();
        $scope.lessons[$scope.activeLessonIndex] = lesson;

        $scope.resetLessonForm();
        $scope.hideLessonDetail();
    };

    $scope.editLesson = function(lessonToEdit, index) {
        $scope.activelesson = lessonToEdit;
        $scope.activeLessonIndex = index;
        $scope.resetLessonForm();
        $scope.showLessonDetail('Lektion editieren');
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
        lessonToDelete.$delete();
        $scope.lessons.splice(index, 1);
    }

    $scope.isUnchanged = function (lessonForm) {
        return angular.equals(lessonForm, $scope.activeLesson);
    };

}]);