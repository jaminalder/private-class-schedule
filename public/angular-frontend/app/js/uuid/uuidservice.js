'use strict';

angular.module('uuid', ['ngResource'])
    .factory('UUIDService', ['$resource', function ($resource) {

        var uuidService = {
            generate: function (callback) {
                $resource('/api/id/generate').get(function (newID) {
                    callback(newID._id);
                });
            }
        };

        return uuidService;

    }]);
