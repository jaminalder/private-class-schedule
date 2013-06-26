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

        console.log('lessonForm: ' + JSON.stringify($scope.lessonForm));
        console.log('lessonForm typeof start: ' + typeof $scope.lessonForm.start);

     // testing of date output
        var startDateString = new Date(angular.copy($scope.lessonForm.start))   ;
        console.log('startDateString ' + typeof startDateString + ' : ' + startDateString);
        var startDate = Date.parse(startDateString);
        console.log('startDate ' + typeof startDate + ' : ' + startDate);
     // end of date output testing
        var lesson = $filter('date')(angular.copy($scope.lessonForm));
     // output for lesson
        var lessonStart = new Date(lesson.start);
        console.log('lessonStart = ' + lessonStart)     ;
        console.log('lesson ' + typeof lesson  + ' + ' + typeof lesson.start + ' start: ' + lesson.start + ' end: ' + lesson.end);
        console.log('lesson ' + typeof lesson  + ' + ' + typeof lesson.start + ' start: ' + $filter('date', 'format: yyyy-MM-dd HH:mm')(lesson.start) + ' end: ' + $filter('date')(lesson.end));
        console.log('lesson ' + typeof lesson  + ' + ' + typeof lesson.start + ' start: ' + new Date(lesson.start) + ' end: ' + $filter('date')(lesson.end));
     // end of output for lesson

        console.log('lesson to save on create: ' + JSON.stringify(lesson));

        LessonService.save(lesson);
        $scope.lessons[$scope.activeLessonIndex] = lesson;

        $scope.resetLessonForm();
        $scope.hideLessonDetail();
    };

    $scope.editLesson = function(lessonToEdit, index) {
        console.log('editLesson ' + typeof lessonToEdit.start + ' start ' + lessonToEdit.start )
        var lessonStart = new Date(lessonToEdit.start);
        var lessonStartString = lessonStart.toDateString();
        var lessonStartToString = lessonStart.toString();
        var lessonEnd = new Date(lessonToEdit.end);
        var lessonEndString = lessonEnd.toDateString();
        var lessonEndStringUTC = lessonEnd.toUTCString();
        var lessonEndStringISO = lessonEnd.toISOString();
        var lessonEndJSON = lessonEnd.toJSON();
        console.log('lessonStart ' + typeof lessonStart + ' start ' + lessonStart) ;
        console.log('lessonStartToString ' + typeof lessonStartToString + ' start ' + lessonStartToString) ;
        console.log('lessonEnd ' + typeof lessonEnd + ' end ' + lessonEnd) ;
        console.log('lessonEndString ' + typeof lessonEndString + ' end ' + lessonEndString) ;
        console.log('lessonEndStringUTC ' + typeof lessonEndStringUTC + ' end ' + lessonEndStringUTC) ;
        console.log('lessonEndStringISO ' + typeof lessonEndStringISO + ' end ' + lessonEndStringISO) ;
        console.log('lessonEndJSON ' + typeof lessonEndJSON + ' end ' + lessonEndJSON) ;
//        $scope.activelesson = {id: lessonToEdit.id, start:lessonStartString, end:lessonEndString};
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
        LessonService.delete(lessonToDelete);
        $scope.lessons.splice(index, 1);
    }

    $scope.isUnchanged = function (lessonForm) {
        return angular.equals(lessonForm, $scope.activeLesson);
    };

}]);