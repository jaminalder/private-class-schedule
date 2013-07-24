'use strict';

angular.module('pcs')
    .factory('AlertService', function ($rootScope) {

        var addAlert = function (type, title, content, closeAfter) {
            $rootScope.alerts.alertArray.push({
                "type": type,
                "title": title,
                "content": content,
                "closeAfter": closeAfter
            });
        }

        var service = {

            addSuccess: function(content) {
                addAlert("success", "", content, 3000);
            },

            addError: function(title, content) {
                addAlert("error", title, content, 8000);
            }



        };

        return service;
    });

