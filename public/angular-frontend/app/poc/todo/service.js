'use strict';

angular.module('todoService', ['ngResource']).
    factory('TodoService', function ($resource) {
        return $resource('/api/todos', {}, {
            getAll: {method: 'GET', params: {}, isArray: true}
        });
    }).factory('AddTodo', function ($resource) {
        return $resource('/api/addTodo/:name', {}, {});
    });
