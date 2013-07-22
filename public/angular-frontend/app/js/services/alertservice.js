'use strict';

angular.module('pcs')
    .factory('AlertService', function ($rootScope) {

        $rootScope.alerts = [];

        var service = {

            addAlert: function (type, content) {
                $rootScope.alerts.push({
                    "type": type,
                    "content": content
                });
            }
        };

        return service;
    });

