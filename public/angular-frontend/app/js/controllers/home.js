'use strict';

angular.module('pcs').controller('HomeCtrl', ['$scope', '$resource', 'students', 'lessons',

    function ($scope, $resource, students, lessons) {
        $scope.lessons = lessons;
        $scope.students = students;

        $scope.setRightViewStudentList = function () {
            $scope.rightView = "studentList"
        }

        $scope.setRightViewLessonList = function () {
            $scope.rightView = "lessonList"
        }

        $scope.setRightViewStudentList();

        $scope.setLeftViewDefault = function () {
            $scope.leftView = "calendar"
        }

        $scope.setLeftViewDefault();

        $scope.setLeftViewStudentForm = function (title) {
            $scope.studentDetailTitle = title;
            $scope.leftView = "studentForm"
        }

        $scope.resetStudentForm = function () {
            $scope.studentForm = angular.copy($scope.activeStudent);
        }

        $scope.setActiveStudent = function (activeStudent, index) {
            $scope.activeStudent = activeStudent;
            $scope.activeStudentIndex = index;
        }

        $scope.setLeftViewLessonForm = function (title) {
            $scope.lessonDetailTitle = title;
            $scope.leftView = "lessonForm"
        }

        $scope.resetLessonForm = function () {
            $scope.lessonForm = angular.copy($scope.activeLesson);
        }

        $scope.setActiveLesson = function (activeStudent, index) {
            $scope.activeLesson = activeStudent;
            $scope.activeLessonIndex = index;
        }

    }]);
