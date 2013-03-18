angular.module('todo', ['ngResource'])

function TodoCtrl($scope, $resource) {

    var ToDo = $resource('/api/poc/todo/:action/:todoId', {todoId:'@id'}, {
        all: {method:'GET', params:{'action':'all'}, isArray:true},
        save: {method:'POST', params:{'action':'save'}},
        delete: {method:'POST', params:{'action':'delete'}}
    });

    $scope.todos = ToDo.all();

    $scope.addTodo = function() {
        var newTodo = new ToDo({id:undefined, text:$scope.todoText, done:false});
        newTodo.$save();
        console.log("new todo: " + JSON.stringify(newTodo));
        $scope.todos.push(newTodo);
        $scope.todoText = '';
    };

    $scope.updateTodo = function(todo){
        todo.$save();
    }

    $scope.remaining = function() {
        var count = 0;
        angular.forEach($scope.todos, function(todo) {
            count += todo.done ? 0 : 1;
        });
        return count;
    };

    $scope.archive = function() {
        var oldTodos = $scope.todos;
        $scope.todos = [];
        angular.forEach(oldTodos, function(todo) {
            if (!todo.done) {
                console.log("keep: " + JSON.stringify(todo));
                $scope.todos.push(todo);
            } else {
                todo.$delete();
                console.log("deleted: " + JSON.stringify(todo));
            }
        });
    };
}