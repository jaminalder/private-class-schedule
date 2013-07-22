'use strict';

angular.module('pcs').controller('StudentListCtrl',

    function ($scope, StudentService, AlertService) {

        console.log('students at StudentListCtrl entry: ' + JSON.stringify($scope.students));

        $scope.editStudent = function (studentToEdit, index) {
            $scope.setActiveStudent(studentToEdit, index);
            $scope.resetStudentForm();
            $scope.setLeftViewStudentForm('Schüler editieren');
        }

        $scope.newStudent = function () {
            $scope.setActiveStudent({}, $scope.students.length);
            $scope.resetStudentForm();
            $scope.setLeftViewStudentForm('Neuer Schüler');
        }

        $scope.deleteStudent = function (studentToDelete, index) {
            StudentService.delete(studentToDelete);
            $scope.students.splice(index, 1);
            AlertService.addAlert("success", "Schüler " + studentToDelete.firstName + " " + studentToDelete.lastName + " gelöscht.");
        }

    });

angular.module('pcs').controller('StudentFormCtrl',

    function ($scope, $resource, UUIDService, StudentService, AlertService) {

        $scope.hideStudentDetail = function () {
            $scope.setLeftViewDefault();
        }

        $scope.saveStudent = function () {

            var studentFormToSave = angular.copy($scope.studentForm);

            var saveStudentWithId = function (id) {

                var student = {
                    id: id,
                    ownerID: $scope.user.id,

                    firstName: studentFormToSave.firstName,
                    lastName: studentFormToSave.lastName,

                    eMail: studentFormToSave.eMail || "",
                    address: studentFormToSave.address || {
                        street: "",
                        streetNum: "",
                        city: "",
                        zip: ""
                    }
                }

                StudentService.save(student);
                $scope.students[$scope.activeStudentIndex] = student;
                AlertService.addAlert("success", "Der Schüler wurde erfolgreich gespeichert");

            }

            if (studentFormToSave.id === undefined) {
                UUIDService.generate(saveStudentWithId);
            } else {
                saveStudentWithId(studentFormToSave.id);
            }

            $scope.resetStudentForm();
            $scope.hideStudentDetail();
        }

        $scope.cancelStudentForm = function () {
            $scope.resetStudentForm();
            $scope.hideStudentDetail();
        }

        $scope.isUnchanged = function (studentForm) {
            return angular.equals(studentForm, $scope.activeStudent);
        };

    });
