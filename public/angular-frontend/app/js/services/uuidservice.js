'use strict';

angular.module('uuid', ['ngResource'])
    .factory('UUIDService', ['$resource', function ($resource) {

        var uuidService = {
            generate: $resource('/api/id/generate').get
        };

        return uuidService;

    }]);
