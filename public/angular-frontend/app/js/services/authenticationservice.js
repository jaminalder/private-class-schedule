'use strict';

angular.module('authenticationServices', [])
    .factory('AuthenticationService', function ($http, $rootScope) {

        var service = {

            registerUserAsTeacher: function (userToRegister, success, error) {
                $http.post('/api/authentication/registerUserAsTeacher', userToRegister)
                    .success(function (registeredUser) {
                        $rootScope.loggedInUser = registeredUser;
                        success(registeredUser);
                    })
                    .error(function () {
                        $rootScope.loggedInUser = undefined;
                        error
                    });
            },

            loginUserAsTeacher: function (userToLogin, success, error) {
                $http.post('/api/authentication/loginUserAsTeacher', userToLogin)
                    .success(function (loggedInUser) {
                        $rootScope.loggedInUser = loggedInUser;
                        success(loggedInUser);
                    })
                    .error(function () {
                        $rootScope.loggedInUser = undefined;
                        error
                    });
            },

            logoutUser: function () {
                $http.post('/api/authentication/logoutUser')
                    .success(function () {
                        $rootScope.loggedInUser = undefined;
                    })
                    .error(function () {
                        // this one shouldn't fail, anyway logout
                        $rootScope.loggedInUser = undefined;
                    });
            },

            getLoggedInUserAsTeacher: function (success, error) {
                $http.get('/api/authentication/getLoggedInUserAsTeacher')
                    .success(function (loggedInUser) {
                        $rootScope.loggedInUser = loggedInUser;
                        success(loggedInUser);
                    })
                    .error(function () {
                        // $rootScope.loggedInUser = undefined;
                        error
                    });
            }
        };

        return service;
    });

