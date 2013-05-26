'use strict';

angular.module('pcs.routes', []).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/student', {templateUrl: 'view/student/studentListAndFormPage.html', controller:StudentCtrl});
    $routeProvider.otherwise({redirectTo: '/student'});
  }]);
