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
            AuthenticationService.register(userToRegister, onSuccess, onError);
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
            AuthenticationService.login(userToLogin, onSuccess, onError);
        };

        $scope.isLoggedIn = function () {
            return AuthenticationService.isLoggedIn();
        };

        $scope.loggedInUserName = $rootScope.user.firstName + ' ' + $rootScope.user.lastName

        $scope.logout = AuthenticationService.logout;

    }]);

angular.module('pcs')
    .controller('AppCtrl',
        ['$rootScope', '$scope', '$location', 'AuthenticationService', function ($rootScope, $scope, $location, AuthenticationService) {

            $scope.getUserRoleText = function (role) {
                return _.invert(AuthenticationService.userRoles)[role];
            };

            $scope.logout = function () {
                AuthenticationService.logout(function () {
                    $location.path('/login');
                }, function () {
                    $rootScope.error = "Failed to logout";
                });
            };
        }]);





