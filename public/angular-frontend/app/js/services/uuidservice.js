'use strict';

angular.module('pcs')
    .factory('UUIDService', ['$resource', function ($resource) {

        var uuidService = {
            generate: $resource('/api/id/generate').get
        };

        return uuidService;

    }]);
