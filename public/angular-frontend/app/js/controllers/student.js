'use strict';

angular.module('pcs').controller('StudentCtrl', ['$scope', '$resource', 'UUIDService','StudentService', 'AuthenticationService', 'students',

function($scope, $resource, UUIDService, StudentService, AuthenticationService, students) {

//    var loggedInUser = AuthenticationService.loggedInUser();
//    $scope.students = StudentService.allStudentsOfTeacher({'ownerID': loggedInUser.id._id});

    $scope.students = students; //from routes resolve

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
            var emptyStudent = new StudentService();

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

}]);
