'use strict';

var studentServices = angular.module('studentServices', ['ngResource']);

studentServices.factory('StudentService', ['$resource', function ($resource) {
    var StudentService = $resource('/api/student/:action/:ownerID', {}, {
        allStudentsOfTeacher: {method: 'GET', params: {'action': 'allStudentsOfTeacher'}, isArray: true},
        save: {method: 'POST', params: {'action': 'saveStudent'}},
        delete: {method: 'POST', params: {'action': 'deleteStudent'}}
    });
    return StudentService;
}]);

