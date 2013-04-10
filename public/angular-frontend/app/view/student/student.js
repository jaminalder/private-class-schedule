'use strict';

angular.module('student', ['ngResource']);

function StudentCtrl($scope, $resource) {

    // UUID and User should somehow be injected later...
    var UUID = $resource('/api/id/generate');
    var User = $resource('/api/user/login/dummy.user@email.com/dummyPassword');

    var Student = $resource('/api/student/:action/:ownerID', {}, {
        allForTeacher: {method: 'GET', params: {'action': 'allForTeacher'}, isArray: true},
        save: {method: 'POST', params: {'action': 'save'}},
        delete: {method: 'POST', params: {'action': 'delete'}}
    });


    User.get(function (userResult) {
        console.log('user result: ' + JSON.stringify(userResult));
        $scope.user = userResult;
        $scope.students = Student.allForTeacher({'ownerID':userResult._id});
    });

    $scope.studentForm = new Student();

    $scope.addStudent = function () {

        UUID.get(function(newID) {
            console.log('uuid result: ' + newID._id);
            var student = angular.copy($scope.studentForm);
            student._id = newID._id;
            student.ownerID = $scope.user._id;
            student.$save();
            $scope.students.push(student);
            $scope.studentForm = new Student();
        });

    };
};
