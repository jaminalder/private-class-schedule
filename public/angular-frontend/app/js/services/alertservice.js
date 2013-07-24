'use strict';

angular.module('pcs')
    .factory('AlertService', function ($rootScope) {

        var service = {

            addAlert: function (type, content) {
                $rootScope.alerts.alertArray.push({
                    "type": type,
                    "content": content
                });

                console.log("addAlert end $rootScope.alerts.alertArray: " + JSON.stringify($rootScope.alerts.alertArray));
            }
        };

        return service;
    });

