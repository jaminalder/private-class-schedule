'use strict';


angular.module('pcs').controller('NavbarController', ['$scope', '$rootScope', 'AuthenticationService',

    function($scope, $rootScope, AuthenticationService) {
        $scope.isLoggedIn = AuthenticationService.isLoggedIn
    }]);

