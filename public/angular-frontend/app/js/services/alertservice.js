'use strict';

angular.module('pcs')
    .factory('AlertService', function ($rootScope) {

        var service = {

            addAlert: function (type, content) {
                $rootScope.alerts.alertArray.push({
                    "type": type,
                    "content": content
                });
            }
        };

        return service;
    });

