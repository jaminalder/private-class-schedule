'use strict';

angular.module('pcs').controller('StudentListCtrl',

    function ($scope, StudentService, AlertService) {

        $scope.editStudent = function (studentToEdit, index) {
            $scope.setActiveStudent(studentToEdit, index);
            $scope.resetStudentForm();
            $scope.setLeftViewStudentForm('Schüler editieren', 'Bitte Details für den Schüler ändern');
        }

        $scope.newStudent = function () {
            $scope.setActiveStudent({}, $scope.students.length);
            $scope.resetStudentForm();
            $scope.setLeftViewStudentForm('Neuer Schüler', 'Bitte neuen Schüler erfassen');
        }

        $scope.deleteStudent = function (studentToDelete, index) {
            StudentService.delete(studentToDelete);
            $scope.students.splice(index, 1);
            AlertService.addSuccess("Schüler " + studentToDelete.firstName + " " + studentToDelete.lastName + " gelöscht.");
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
                AlertService.addSuccess("Schüler " + student.firstName + " " + student.lastName + " gespeichert");

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
