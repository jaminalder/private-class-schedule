'use strict';

var lessonServices = angular.module('pcs');

lessonServices.factory('LessonService', ['$resource', function ($resource) {
    var LessonService = $resource('/api/lesson/:action/:teacherId', {}, {
        allLessonsOfTeacher: {method: 'GET', params: {'action': 'allLessonsOfTeacher'}, isArray: true},
        save: {method: 'POST', params: {'action': 'saveLesson'}},
        delete: {method: 'POST', params: {'action': 'deleteLesson'}}
    });
    return LessonService;
}]);


