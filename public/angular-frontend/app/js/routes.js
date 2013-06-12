'use strict';

var pcsroutes = angular.module('pcsroutes', []).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/', {
                templateUrl: 'view/calendar/calendar.html',
                controller: CalendarCtrl
            }).
            when('/student', {
                controller: 'StudentCtrl',
                templateUrl: 'view/student/studentListAndFormPage.html'
            }).
            when('/calendar', {
                templateUrl: 'view/calendar/calendar.html',
                controller: CalendarCtrl}).
            when('/calendarStudentList', {
                templateUrl: 'view/calendar/calendarStudentList.html',
                controller: (CalendarCtrl, 'StudentCtrl')
            }).
            when('/lesson', {
                controller: 'LessonCtrl',
                resolve: {
                    Lesson: function (LessonService) {
                        return LessonService();
                    }
                },
                templateUrl: 'view/lesson/lesson.html'
            }).
            when('/register', {
                controller: 'RegisterController',
                templateUrl: 'view/authentication/registerForm.html'
            }).
            when('/login', {
                controller: 'LoginController',
                templateUrl: 'view/authentication/loginForm.html'
            }).
            otherwise({redirectTo: '/'});
    }]);
