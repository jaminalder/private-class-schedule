/**
 * calendarDemoApp - 0.1.3
 * https://github.com/angular-ui/ui-calendar
 * MIT License
 */

'use strict';

angular.module('pcs').controller('CalendarCtrl', ['$scope', '$filter', 'UserService', 'LessonService',

    function ($scope, $filter, UserService, LessonService) {

        $scope.eventSources = [];
        $scope.lessonEvents = [];
        $scope.eventSources.push($scope.lessonEvents);

        $scope.$on('LessonsChangedEvent', function () {
            $scope.lessonEvents.length = 0;
            var i
            for (i in $scope.lessons) {
                var lessonId = $scope.lessons[i].id._id;
                var lessonStart = $scope.lessons[i].start / 1000;
                var lessonEnd = $scope.lessons[i].end / 1000;
                var studentIds = $scope.lessons[i].studentIds;
                $scope.lessonEvents.push({
                    id: lessonId,
                    title: '',
                    start: lessonStart,
                    end: lessonEnd,
                    studentIds: studentIds,
                    allDay: false});
            }
        });

        $scope.fireLessonsChangedEvent();

        $scope.dayClick = function (date, allDay, jsEvent, view) {
            $scope.$apply(function () {
                console.log('dayClick ' + date);
                $scope.alertMessage = ('Day Clicked ' + date);
            });
        };

        $scope.eventClick = function (event, jsEvent, view) {
            $scope.$apply(function () {
                console.log('eventClick ' + event);
                var lessonToEdit = createLessonFromEvent(event);
                $scope.editLesson(lessonToEdit);
            });
        };

        $scope.eventDrop = function (event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view) {
            $scope.$apply(function () {
                console.log('eventDrop ' + event);
                saveLessonFromEvent(event);
            });
        };

        $scope.eventResize = function (event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view) {
            $scope.$apply(function () {
                console.log('eventResize ' + event);
                saveLessonFromEvent(event);
            });
        };

        var saveLessonFromEvent = function (event) {
            var lesson = createLessonFromEvent(event);
            LessonService.save(lesson);
            $scope.lessons[$scope.activeLessonIndex] = lesson;
        }

        var createLessonFromEvent = function (event) {
            var lesson = {};
            lesson.id = event.id === undefined ? undefined : {_id: event.id};
            lesson.teacherId = $scope.user.id;
            lesson.start = Date.parse(event.start);
            lesson.end = Date.parse(event.end);
            lesson.studentIds = event.studentIds === undefined ? [] : event.studentIds;
            return lesson;
        }

        /* add and removes an event source of choice */
        $scope.addRemoveEventSource = function (sources, source) {
            var canAdd = 0;
            angular.forEach(sources, function (value, key) {
                if (sources[key] === source) {
                    sources.splice(key, 1);
                    canAdd = 1;
                }
            });
            if (canAdd === 0) {
                sources.push(source);
            }
        };
        /* add custom event*/
        $scope.addEvent = function () {
            $scope.events.push({
                title: 'Open Sesame',
                start: new Date(y, m, 28),
                end: new Date(y, m, 29),
                className: ['openSesame']
            });
        };
        /* remove event */
        $scope.remove = function (index) {
            $scope.events.splice(index, 1);
        };

        $scope.select = function (start, end, allDay) {
            console.log('select ' + start);
            $scope.$apply(function () {
                var event = {
                    start: start,
                    end: end,
                    allDay: allDay
                }
                var lessonToEdit = createLessonFromEvent(event);
                $scope.selectNewLesson(lessonToEdit);
            });
            $scope.pcsCalendar.fullCalendar('unselect');
        };

        /* config object */
        $scope.uiConfig = {
            calendar: {
                height: 600, /* height of calendar */
                header: {
                    left: 'today,prevYear,prev,next,nextYear',
                    center: 'title',
                    right: 'agendaDay,agendaWeek,month'
                },
                buttonText: {
                    today: 'Heute',
                    prevYear: '&lt;&lt;',
                    prev: '&lt;',
                    next: '&gt;',
                    nextYear: '&gt;&gt;',
                    agendaDay: 'Tag',
                    agendaWeek: 'Woche',
                    month: 'Monat'
                },
                theme: false,
                defaultView: 'agendaWeek',
                monthNames: ['Januar', 'Februar', 'März', 'April', 'Mai', 'Juni', 'Juli',
                    'August', 'September', 'Oktober', 'November', 'Dezember'],
                monthNamesShort: ['Jan', 'Feb', 'Mrz', 'Apr', 'Mai', 'Jun', 'Jul',
                    'Aug', 'Sep', 'Okt', 'Nov', 'Dez'],
                dayNames: ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch',
                    'Donnerstag', 'Freitag', 'Samstag'],
                dayNamesShort: ['So', 'Mo', 'Di', 'Mi',
                    'Do', 'Fr', 'Sa'],
                titleFormat: {
                    month: 'MMMM yyyy',                                 // September 2009
                    week: "d. [MMMM] [ yyyy]{ '&#8212;' d. MMMM yyyy}", // 7. - 13. September 2009
                    day: 'dddd, d. MMMM yyyy'                           // Dienstag, 8. September 2009
                },
                columnFormat: {
                    month: 'ddd',    // Mo
                    week: 'ddd, d.M.', // Mo, 7.9.
                    day: 'dddd, d.M.'  // Montag, 7.9.
                },
                allDayText: 'Ganzt.',
                axisFormat: 'H:mm',
                firstDay: 1,
                timeFormat: {
                    agenda: 'H:mm{ - H:mm}',
                    '': 'H:mm'
                },
                snapMinutes: 5, /* events can be moved in 5 minute steps, default slotMinutes */
                slotMinutes: 30, /* calender display in 30 minutes intervall, default slotMinutes = 30 minutes */
                editable: true,
                selectable: true,
                selectHelper: true,
                select: $scope.select,
                dayClick: $scope.dayClick,
                eventClick: $scope.eventClick,
                eventDrop: $scope.eventDrop,
                eventResize: $scope.eventResize,
                eventRender: function (event, element) {
                    if (event.studentIds !== undefined) {
                        var newDisplayString = "";
                        for (var i = 0; i < event.studentIds.length; i++) {
                            var student = $scope.findStudentById(event.studentIds[i]._id);
                            if (student !== undefined) {
                                if(i>0){
                                    newDisplayString += ", ";
                                }
                                newDisplayString += student.firstName + " " + student.lastName;
                            }
                        }
                        element.find('div.fc-event-title').append(newDisplayString);
                    }
                }
            }
        };

    }]);



