'use strict';

angular.module('pcs').factory('LessonService', function ($resource) {
    var LessonService = $resource('/api/lesson/:action/:teacherId', {}, {
        allLessonsOfTeacher: {method: 'GET', params: {'action': 'allLessonsOfTeacher'}, isArray: true},
        save: {method: 'POST', params: {'action': 'saveLesson'}},
        delete: {method: 'POST', params: {'action': 'deleteLesson'}}
    });
    return LessonService;
});

angular.module('pcs').factory('LessonLoader', function ($http, $q, AuthenticationService, LessonService) {

    return {
        allLessonsOfTeacher: function () {
            return AuthenticationService.getLoggedInUser().then(function (loggedInUser) {
                var deferred = $q.defer();
                LessonService.allLessonsOfTeacher({'teacherId': loggedInUser.id._id}, function (lessons) {
                    deferred.resolve(lessons);
                });
                return deferred.promise;
            });
        }
    }
});



