'use strict';

angular.module('pcs.routes', []).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/student', {templateUrl: 'view/student/studentListAndFormPage.html', controller:StudentCtrl});
    $routeProvider.when('/calendar', {templateUrl: 'view/calendar/calendar.html', controller:CalendarCtrl});
    $routeProvider.when('/calendarStudentList', {templateUrl: 'view/calendar/calendarStudentList.html', controller:(CalendarCtrl, StudentCtrl)});
    $routeProvider.otherwise({redirectTo: '/student'});
  }]);
