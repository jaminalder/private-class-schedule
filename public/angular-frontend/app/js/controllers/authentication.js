'use strict';

var authModule = angular.module('pcs');

authModule.controller('RegisterController', ['$scope', '$resource', '$location', 'UUIDService', 'AuthenticationService', 'AlertService',

    function ($scope, $resource, $location, UUIDService, AuthenticationService, AlertService) {


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
                $location.path('/');
                AlertService.addSuccess(registeredUser['firstName'] + " " + registeredUser['lastName'] + " erfolgreich registriert");
            };
            var onError = function () {
                AlertService.addError("Registrierung fehlgeschlagen!", "Es existiert schon ein Benutzer mit dieser E-Mail Adresse.");
            };
            AuthenticationService.register(userToRegister, onSuccess, onError);
        };
    }]);

authModule.controller('LoginController', ['$scope', '$rootScope', '$location', '$resource', 'UUIDService', 'AuthenticationService', 'AlertService',

    function ($scope, $rootScope, $location, $resource, UUIDService, AuthenticationService, AlertService) {


        $scope.resetLoginForm = function () {
            $scope.loginForm = {};
        };

        $scope.resetLoginForm();

        $scope.loginUserAsTeacher = function () {
            var userToLogin = angular.copy($scope.loginForm);
            var onSuccess = function (loggedInUser) {
                $location.path('/');
                AlertService.addSuccess(loggedInUser['firstName'] + " " + loggedInUser['lastName'] + " erfolgreich angemeldet");
            };
            var onError = function () {
                AlertService.addError("Anmeldung fehlgeschlagen!", "E-Mail oder Passwort war nicht korrekt.");
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
        ['$rootScope', '$scope', '$location', 'AuthenticationService', 'AlertService',
            function ($rootScope, $scope, $location, AuthenticationService, AlertService) {

            $scope.getUserRoleText = function (role) {
                return _.invert(AuthenticationService.userRoles)[role];
            };

            $scope.logout = function () {
                AuthenticationService.logout(function () {
                    $location.path('/login');
                    AlertService.addSuccess("Benutzer abgemeldet.");
                }, function () {
                    AlertService.addError("Abmelden fehlgeschlagen!", "");
                });
            };
        }]);





