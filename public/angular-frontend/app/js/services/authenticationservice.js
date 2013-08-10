'use strict';

angular.module('pcs')
    .factory('AuthenticationService', function ($http, $rootScope, $cookieStore, $q) {

        var accessLevels = {
            public: 0,
            teacher: 1
        }

        $rootScope.user = { role: accessLevels.public };
        //$rootScope.user = $cookieStore.get('user') || { username: '', role: userRoles.public };
        //$cookieStore.remove('user');

        var service = {

            accessLevels: accessLevels,

            authorize: function (accessLevel) {
                return accessLevel === accessLevels.public || $rootScope.user.role === accessLevel;
            },
            isLoggedIn: function () {
                return $rootScope.user.role === accessLevels.teacher;
            },
            getLoggedInUser: function(){
                var deferred = $q.defer();

                if($rootScope.user.role === accessLevels.teacher) {
                    deferred.resolve($rootScope.user);
                }

                $http.get('/api/authentication/getLoggedInUserAsTeacher')
                    .success(function (loggedInUser) {
                        loggedInUser.role = accessLevels.teacher;
                        $rootScope.user = loggedInUser;
                        deferred.resolve(loggedInUser);
                    })
                    .error(function () {
                        $rootScope.user.role = accessLevels.public;
                        deferred.reject();
                    });

                return deferred.promise;

            },
            register: function (user, success, error) {
                $http.post('/api/authentication/registerUserAsTeacher', user).success(success).error(error);
            },
            login: function (userToLogin, success, error) {
                $http.post('/api/authentication/loginUserAsTeacher', userToLogin).success(function (loggedInUser) {
                    loggedInUser.role = accessLevels.teacher;
                    //console.log('loggedInUserWithRole: ' + JSON.stringify(loggedInUser));
                    $rootScope.user = loggedInUser;
                    success(loggedInUser);
                }).error(error);
            },
            logout: function (success, error) {
                $http.post('/api/authentication/logoutUser').success(function () {
                    $rootScope.user.role = accessLevels.public;
                    success();
                }).error(error);
            }
        };

        return service;
    });

