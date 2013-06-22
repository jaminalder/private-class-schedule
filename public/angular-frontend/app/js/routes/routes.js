'use strict';

angular.module('pcs')
    .config(['$routeProvider', '$locationProvider', '$httpProvider', function ($routeProvider, $locationProvider, $httpProvider) {

        var access = {
            public: 0,
            teacher: 1
        }

        $routeProvider.when('/', {
            templateUrl: '/app/view/calendar/calendar.html',
            controller: CalendarCtrl,
            access: access.public
        });
        $routeProvider.when('/student', {
            controller: 'StudentCtrl',
            templateUrl: '/app/view/student/studentListAndFormPage.html',
            access: access.teacher
        });
        $routeProvider.when('/calendar', {
            templateUrl: '/app/view/calendar/calendar.html',
            controller: CalendarCtrl,
            access: access.public
        });
        $routeProvider.when('/calendarStudentList', {
            templateUrl: '/app/view/calendar/calendarStudentList.html',
            controller: (CalendarCtrl, 'StudentCtrl'),
            access: access.public
        });
        $routeProvider.when('/calendarLessonList', {
            templateUrl: '/app/view/calendar/calendarLessonList.html',
            controller: (CalendarCtrl, 'LessonCtrl'),
            access: access.public
        });
        $routeProvider.when('/lesson', {
            controller: 'LessonCtrl',
            resolve: {
                Lesson: function (LessonService) {
                    return LessonService();
                }
            },
            templateUrl: '/app/view/lesson/lesson.html',
            access: access.public
        });
        $routeProvider.when('/register', {
            controller: 'RegisterController',
            templateUrl: '/app/view/authentication/registerForm.html',
            access: access.public
        });
        $routeProvider.when('/login', {
            controller: 'LoginController',
            templateUrl: '/app/view/authentication/loginForm.html',
            access: access.public
        });
        $routeProvider.when('/404',
            {
                templateUrl: '/app/view/errors/404.html',
                access: access.public
            });
        $routeProvider.otherwise({redirectTo: '/404'});

        // $locationProvider.html5Mode(true);

        var interceptor = ['$location', '$q', function ($location, $q) {
            function success(response) {
                return response;
            }

            function error(response) {

                if (response.status === 401) {
                    $location.path('/login');
                    return $q.reject(response);
                }
                else {
                    return $q.reject(response);
                }
            }

            return function (promise) {
                return promise.then(success, error);
            }
        }];

        $httpProvider.responseInterceptors.push(interceptor);

    }]);

angular.module('pcs')
    .run(['$rootScope', '$location', 'AuthenticationService', function ($rootScope, $location, AuthenticationService) {

        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            $rootScope.error = null;
            if (next.access !== undefined && !AuthenticationService.authorize(next.access)) {
                console.log('routeChange not authorized to: ' + JSON.stringify(next));
                if (AuthenticationService.isLoggedIn()){
                    console.log('logged in user is not authorized. redirect to user home');
                    $location.path('/');
                } else {
//                    $location.path('/login');

                    AuthenticationService.loggedInUserFromServer(function(loggedInUser){
                        console.log('found logged in user on server: ' + JSON.stringify(loggedInUser));
                        $location.path('/student');
                    }, function(){
                        console.log('did not find logged in user on server. redirect to login');
                        $location.path('/login');
                    });

                }
            }
        });

        $rootScope.appInitialized = true;
    }]);