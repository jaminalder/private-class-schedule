'use strict';

angular.module('pcs')
    .config(['$routeProvider', '$locationProvider', '$httpProvider', function ($routeProvider, $locationProvider, $httpProvider) {

        var access = routingConfig.accessLevels;

        $routeProvider.when('/', {
            templateUrl: 'view/calendar/calendar.html',
            controller: CalendarCtrl,
            access: access.anon
        });
        $routeProvider.when('/student', {
            controller: 'StudentCtrl',
            templateUrl: 'view/student/studentListAndFormPage.html',
            access: access.anon
        })
        $routeProvider.when('/calendar', {
            templateUrl: 'view/calendar/calendar.html',
            controller: CalendarCtrl,
            access: access.anon
        });
        $routeProvider.when('/calendarStudentList', {
            templateUrl: 'view/calendar/calendarStudentList.html',
            controller: (CalendarCtrl, 'StudentCtrl'),
            access: access.anon
        });
        $routeProvider.when('/lesson', {
            controller: 'LessonCtrl',
            resolve: {
                Lesson: function (LessonService) {
                    return LessonService();
                }
            },
            templateUrl: 'view/lesson/lesson.html',
            access: access.anon
        });
        $routeProvider.when('/register', {
            controller: 'RegisterController',
            templateUrl: 'view/authentication/registerForm.html',
            access: access.anon
        });
        $routeProvider.when('/login', {
            controller: 'LoginController',
            templateUrl: 'view/authentication/loginForm.html',
            access: access.anon
        });
        $routeProvider.when('/404',
            {
                templateUrl: 'app/view/errors/404.html',
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
            if (!AuthenticationService.authorize(next.access)) {
                if (AuthenticationService.isLoggedIn()) $location.path('/');
                else                  $location.path('/login');
            }
        });

        $rootScope.appInitialized = true;
    }]);

