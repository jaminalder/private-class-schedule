'use strict';

angular.module('pcs').controller('HomeCtrl',

    function ($scope, $resource, students, lessons, DateTimeService) {
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

        $scope.setLeftViewStudentForm = function (title, detailtext) {
            $scope.studentDetailTitle = title;
            $scope.studentDetailText = detailtext;
            $scope.leftView = "studentForm"
        }

        $scope.resetStudentForm = function () {
            $scope.studentForm = angular.copy($scope.activeStudent);
        }

        $scope.setActiveStudent = function (activeStudent, index) {
            $scope.activeStudent = activeStudent;
            $scope.activeStudentIndex = index;
        }

        $scope.setLeftViewLessonForm = function (title, detailtext) {
            $scope.lessonDetailTitle = title;
            $scope.lessonDetailText = detailtext;
            $scope.leftView = "lessonForm"
        }

        $scope.resetLessonForm = function () {
            $scope.lessonForm = angular.copy($scope.activeLesson);

            //$scope.lessonForm.date = "20.07.2013";
            $scope.lessonForm.date = DateTimeService.getDateMidnight($scope.lessonForm.start);
            $scope.lessonForm.startTime = DateTimeService.getTimeString($scope.lessonForm.start);
            $scope.lessonForm.endTime = DateTimeService.getTimeString($scope.lessonForm.end);

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

        $scope.selectNewLesson = function (lessonToEdit) {
            var durationInMin = (lessonToEdit.end - lessonToEdit.start) / 60000;
            if(durationInMin < 10){
                lessonToEdit.end = lessonToEdit.start + (30 * 60000); // start + 30 min
            }
            $scope.setActiveLesson(lessonToEdit, $scope.lessons.length);
            $scope.resetLessonForm();
            $scope.setLeftViewLessonForm('Neue Lektion');
        }

        $scope.editLesson = function (lessonToEdit, index) {
            if (index === undefined) {
                index = $scope.findLessonIndex(lessonToEdit);
            }
            $scope.setActiveLesson(lessonToEdit, index);
            $scope.resetLessonForm();
            $scope.setLeftViewLessonForm('Lektion editieren', 'Bitte Änderungen an der bestehenden Lektion vornehmen');
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


    });
