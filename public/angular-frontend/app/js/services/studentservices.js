'use strict';

var studentServices = angular.module('pcs');

studentServices.factory('StudentService', function ($resource) {
    var StudentService = $resource('/api/student/:action/:ownerID', {}, {
        allStudentsOfTeacher: {method: 'GET', params: {'action': 'allStudentsOfTeacher'}, isArray: true},
        save: {method: 'POST', params: {'action': 'saveStudent'}},
        delete: {method: 'POST', params: {'action': 'deleteStudent'}},
    });
    return StudentService;
});

angular.module('pcs').factory('StudentLoader', function ($http, $q, AuthenticationService, StudentService) {

    return {
        allStudentsOfTeacher: function () {
            return AuthenticationService.getLoggedInUser().then(function (loggedInUser) {
                var deferred = $q.defer();
                StudentService.allStudentsOfTeacher({'ownerID': loggedInUser.id._id}, function (students) {
                    deferred.resolve(students);
                });
                return deferred.promise;
            });
        }
    }
});




