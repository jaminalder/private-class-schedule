'use strict';

var authModule = angular.module('pcs');

authModule.controller('RegisterController', ['$scope', '$resource', 'UUIDService', 'AuthenticationService',

    function ($scope, $resource, UUIDService, AuthenticationService) {


        $scope.resetRegisterForm = function () {

            var getEmptyUser = function (callback) {
                UUIDService.generate(function (id) {
                    callback({
                        id: id,
                        ownerID: {_id: '42'},
                        address: {
                            street: "",
                            streetNum: "",
                            city: "",
                            zip: ""
                        }
                    });
                });
            };

            getEmptyUser(function (emptyUser) {
                $scope.registerForm = {user: emptyUser}
            });
        };

        $scope.resetRegisterForm();

        $scope.registerUserAsTeacher = function () {
            var userToRegister = angular.copy($scope.registerForm);
            var onSuccess = function (registeredUser) {
                $scope.message = 'User' + registeredUser['firstName'] + ' ' + registeredUser['lastName'] + ' successfully registered'
            };
            var onError = function () {
                $scope.message = 'User registration failed'
            };
            AuthenticationService.registerUserAsTeacher(userToRegister, onSuccess, onError);
        };
    }]);

authModule.controller('LoginController', ['$scope', '$rootScope', '$resource', 'UUIDService', 'AuthenticationService',

    function ($scope, $rootScope, $resource, UUIDService, AuthenticationService) {


        $scope.resetLoginForm = function () {
            $scope.loginForm = {};
        };

        $scope.resetLoginForm();

        $scope.loginUserAsTeacher = function () {
            var userToLogin = angular.copy($scope.loginForm);
            var onSuccess = function (loggedInUser) {
                $scope.message = 'User' + loggedInUser['firstName'] + ' ' + loggedInUser['lastName'] + ' successfully logged in'
            };
            var onError = function () {
                $scope.message = 'User login failed'
            };
            AuthenticationService.loginUserAsTeacher(userToLogin, onSuccess, onError);
        };

        $scope.logoutUser = function () {
            AuthenticationService.logoutUser();
        }

        $scope.isUserLoggedIn = function () {
            if ($rootScope.loggedInUser === undefined) {
                return false;
                // no user is logged in on the client side, now verify it on the server side
                /* todo get this working
                 var onSuccess = function () {
                 return true;
                 }
                 var onError = function () {
                 return false;
                 }
                 AuthenticationService.getLoggedInUserAsTeacher(onSuccess, onError);
                 */
            } else {
                // logged in on the client side
                return true;
            }
        }


    }]);
