'use strict';


angular.module('pcs').controller('NavbarController',

    function($scope, $rootScope) {

        $scope.isUserLoggedIn = function(){
            return $rootScope.loggedInUser !== undefined
        }

    });

