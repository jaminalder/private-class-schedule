'use strict';

angular.module('pcs')
    .factory('AuthenticationService', function ($http, $rootScope, $cookieStore) {

        var accessLevels = routingConfig.accessLevels
        var userRoles = routingConfig.userRoles;

        $rootScope.user = $cookieStore.get('user') || { username: '', role: userRoles.public };
        $cookieStore.remove('user');

        $rootScope.accessLevels = accessLevels;
        $rootScope.userRoles = userRoles;

        var service = {

            authorize: function (accessLevel, role) {
                if (role === undefined)
                    role = $rootScope.user.role;
                return accessLevel & role;
            },
            isLoggedIn: function (user) {
                if (user === undefined)
                    user = $rootScope.user;
                return user.role === userRoles.user || user.role === userRoles.admin;
            },
            register: function (user, success, error) {
                $http.post('/api/authentication/registerUserAsTeacher', user).success(success).error(error);
            },
            login: function (userToLogin, success, error) {
                $http.post('/api/authentication/loginUserAsTeacher', userToLogin).success(function (loggedInUser) {
                    loggedInUser.role = userRoles.user;
                    console.log('loggedInUserWithRole: ' + JSON.stringify(loggedInUser));
                    $rootScope.user = loggedInUser;
                    success(loggedInUser);
                }).error(error);
            },
            logout: function (success, error) {
                $http.post('/api/authentication/logoutUser').success(function () {
                    $rootScope.user.username = '';
                    $rootScope.user.role = userRoles.public;
                    success();
                }).error(error);
            },
            accessLevels: accessLevels,
            userRoles: userRoles
        };

        return service;
    });

angular.module('pcs')
    .factory('User', function ($http) {
        return {
            getLoggedInUser: function (success, error) {
                $http.get('/api/authentication/getLoggedInUserAsTeacher').success(success).error(error);
            }
        };
    });

