angular.module('todo', ['ngResource'])


function TodoCtrl($scope, $resource) {

    var ToDo = $resource('/api/todo/');

    $scope.todos = ToDo.query();

    $scope.addTodo = function() {
        var newTodo = new ToDo({text:$scope.todoText, done:false});
        $scope.todos.push(newTodo);
        newTodo.$save();
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
            if (!todo.done) $scope.todos.push(todo);
        });
    };
}