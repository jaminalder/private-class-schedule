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

            var startDate = new Date($scope.lessonForm.start);
            var endDate = new Date($scope.lessonForm.end);

            $scope.lessonForm.date = $scope.getDateMidnight(startDate);
            $scope.lessonForm.startTime = $scope.getTimeString(startDate);
            $scope.lessonForm.endTime = $scope.getTimeString(endDate);

            console.log('resetLessonForm date: ' + $scope.lessonForm.date);
            console.log('resetLessonForm startTime: ' + $scope.lessonForm.startTime);
            console.log('resetLessonForm endTime: ' + $scope.lessonForm.endTime);

            $scope.lessonForm.lessonStudentIds = [];
            var i
            for (i in $scope.lessonForm.studentIds) {
                $scope.lessonForm.lessonStudentIds.push($scope.lessonForm.studentIds[i]._id);
            }

        }

        $scope.setActiveLesson = function (activeLesson, index) {
            $scope.activeLesson = activeLesson;
            $scope.activeLessonIndex = index;
        }

        $scope.fireLessonsChangedEvent = function () {
            $scope.$broadcast('LessonsChangedEvent');
        }

        $scope.editLesson = function (lessonToEdit, index) {
            console.log('editLesson lessonToEdit: ' + JSON.stringify(lessonToEdit) + ', index: ' + index);
            if (index === undefined) {
                index = $scope.findLessonIndex(lessonToEdit);
            }
            $scope.setActiveLesson(lessonToEdit, index);
            $scope.resetLessonForm();
            $scope.setLeftViewLessonForm('Lektion editieren');
        }

        $scope.findLessonIndex = function (lesson) {
            if (lesson.id !== undefined) {
                for (var i = 0; i < $scope.lessons.length; i++) {
                    if (lesson.id._id === $scope.lessons[i].id._id) {
                        return i;
                    }
                }
            }
            return $scope.lessons.length;
        }

        $scope.findStudentById = function (studentId) {
            if (studentId !== undefined) {
                for (var i = 0; i < $scope.students.length; i++) {
                    if (studentId === $scope.students[i].id._id) {
                        return $scope.students[i];
                    }
                }
            }
            return undefined;
        }

        $scope.getDateMidnight = function (date) {
            var result = new Date(date);
            result.setUTCHours(0, 0, 0, 0);
            return result;
        }

        $scope.getTimeString = function (date) {
            var h = date.getHours() > 9 ? date.getHours() : "0" + date.getHours();
            var m = date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes();
            return h + ":" + m;
        }

    }]);
