'use strict';

var userServices = angular.module('pcs');

userServices.factory('UserService', ['$resource', function ($resource) {
    var User = $resource('/api/user/login/dummy.user@email.com/dummyPassword');
    return User;
}]);

