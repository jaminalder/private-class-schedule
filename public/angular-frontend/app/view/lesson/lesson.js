/**
 * Created with IntelliJ IDEA.
 * User: Arabella
 * Date: 29.05.13
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */

'use strict';

angular.module('lesson', ['ngResource', 'uuid']).directive('date', function (dateFilter) {
    return {
        require:'ngModel',
        link:function (scope, elm, attrs, ctrl) {

            var dateFormat = attrs['date'] || 'yyyy-MM-dd';
            var minDate = Date.parse(attrs['min']) || 0;
            var maxDate = Date.parse(attrs['max']) || 9007199254740992;

            ctrl.$parsers.unshift(function (viewValue) {
                var parsedDateMilissec = Date.parse(viewValue);
                if (parsedDateMilissec > 0) {
                    if (parsedDateMilissec >= minDate && parsedDateMilissec <= maxDate) {
                        ctrl.$setValidity('date', true);
                        return parsedDateMilissec;
                    }
                }

                // in all other cases it is invalid, return undefined (no model update)
                ctrl.$setValidity('date', false);
                return undefined;
            });

            ctrl.$formatters.unshift(function (modelValue) {
                return dateFilter(modelValue, dateFormat);
            });
        }
    };
});


function LessonCtrl($scope, $filter, $resource, UUIDService) {
    // User should somehow be injected later...
    var User = $resource('/api/user/login/dummy.user@email.com/dummyPassword');

    var Lesson = $resource('/api/lesson/:action/:teacherId', {}, {
        allLessonsOfTeacher: {method: 'GET', params: {'action': 'allLessonsOfTeacher'}, isArray: true},
        save: {method: 'POST', params: {'action': 'saveLesson'}},
        delete: {method: 'POST', params: {'action': 'deleteLesson'}}
    });

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
            emptyLesson.studentIds = [""] ;

            //set the optional fields to "" to ensure a proper conversion to person on the server
            //not sure, if this is really a good idea

            callback(emptyLesson);
        }

        UUIDService.generate(constructEmptyLesson);

    };

    $scope.resetLessonForm = function () {
        $scope.lessonForm = angular.copy($scope.activeLesson);
    };


    $scope.saveLesson = function () {
        var startDate = $filter('date')(angular.copy($scope.lessonForm.start))   ;
        var endDate =  $filter('date')(angular.copy($scope.lessonForm.end)  )  ;
        var lesson = angular.copy($scope.lessonForm);
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



};