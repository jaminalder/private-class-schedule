'use strict';

angular.module('student', ['ngResource', 'uuid']);

function StudentCtrl($scope, $resource, UUIDService) {

    // User should somehow be injected later...
    var User = $resource('/api/user/login/dummy.user@email.com/dummyPassword');

    var Student = $resource('/api/student/:action/:ownerID', {}, {
        allStudentsOfTeacher: {method: 'GET', params: {'action': 'allStudentsOfTeacher'}, isArray: true},
        save: {method: 'POST', params: {'action': 'saveStudent'}},
        delete: {method: 'POST', params: {'action': 'deleteStudent'}}
    });


    User.get(function (userResult) {
        console.log('user result: ' + JSON.stringify(userResult));
        $scope.user = userResult;
        $scope.students = Student.allStudentsOfTeacher({'ownerID': userResult.id._id});
    });

    $scope.showStudentDetail = function(title){
        $scope.studentDetailTitle = title;
        $scope.studentDetailVisible = true;
    }

    $scope.hideStudentDetail = function(){
        $scope.studentDetailVisible = false;
    }

    $scope.hideStudentDetail();

    $scope.getEmptyStudent = function (callback) {

        var constructEmptyStudent = function (id) {
            var emptyStudent = new Student();

            emptyStudent.id = id;
            emptyStudent.ownerID = $scope.user.id;

            //set the optional fields to "" to ensure a proper conversion to person on the server
            //not sure, if this is really a good idea
            emptyStudent.eMail = "";
            emptyStudent.address = {
                street: "",
                streetNum: "",
                city: "",
                zip: ""
            };

            callback(emptyStudent);
        }

        UUIDService.generate(constructEmptyStudent);

    };

    $scope.resetStudentForm = function () {
        $scope.studentForm = angular.copy($scope.activeStudent);
    };

    $scope.saveStudent = function () {
        var student = angular.copy($scope.studentForm);

        student.$save();
        $scope.students[$scope.activeStudentIndex] = student;

        $scope.resetStudentForm();
        $scope.hideStudentDetail();
    };

    $scope.editStudent = function(studentToEdit, index) {
        $scope.activeStudent = studentToEdit;
        $scope.activeStudentIndex = index;
        $scope.resetStudentForm();
        $scope.showStudentDetail('Schüler editieren');
    }

    $scope.newStudent = function() {
        $scope.getEmptyStudent(function (emptyStudent) {
            $scope.activeStudent = emptyStudent;
            $scope.activeStudentIndex = $scope.students.length;
            $scope.resetStudentForm();
            $scope.showStudentDetail('Neuer Schüler');
        });
    }

    $scope.deleteStudent = function(studentToDelete, index) {
        studentToDelete.$delete();
        $scope.students.splice(index, 1);
    }

    $scope.isUnchanged = function (studentForm) {
        return angular.equals(studentForm, $scope.activeStudent);
    };

};
