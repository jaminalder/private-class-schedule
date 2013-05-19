'use strict';

angular.module('pcs.routes', []).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/student', {templateUrl: 'view/student/studentListAndFormPage.html', controller:StudentCtrl});
    $routeProvider.when('/view2', {templateUrl: 'partials/partial2.html'});
    $routeProvider.otherwise({redirectTo: '/student'});
  }]);
