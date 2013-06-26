/**
 * calendarDemoApp - 0.1.3
 * https://github.com/angular-ui/ui-calendar
 * MIT License
 */

'use strict';

angular.module('pcs').controller('CalendarCtrl', ['$scope', '$filter', 'UserService', 'LessonService',

    function ($scope, $filter, UserService, LessonService) {


        console.log('lessons at CalendarCtrl entry: ' + JSON.stringify($scope.lessons));

        $scope.eventSources = [];

        $scope.lessonEvents = [];

        // $scope.lessons = lessons;

        var i
        for (i in $scope.lessons) {
            var lessonId = $scope.lessons[i].id._id;
            console.log('lesson id: ' + i + ' : ' + lessonId);
            var lessonStart = $scope.lessons[i].start / 1000;
            var lessonEnd = $scope.lessons[i].end / 1000;
            $scope.lessonEvents.push(
                {id: lessonId, title: 'Lesson ' + i, start: lessonStart, end: lessonEnd, allDay: false});
        }

        $scope.eventSources.push($scope.lessonEvents);

        /* alert on dayClick */
        $scope.alertDayOnClick = function (date, allDay, jsEvent, view) {
            $scope.$apply(function () {
                console.log('Day Clicked ' + date);
                $scope.alertMessage = ('Day Clicked ' + date);
            });
        };

        /* alert on eventClick */
        $scope.alertEventOnClick = function (event, jsEvent, view) {
            $scope.$apply(function () {
                console.log('Event Clicked ' + event);
                $scope.alertMessage = ('Event Clicked ' + event);
            });
        };

        /* alert on Drop */
        $scope.eventChangeOnDrop = function (event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view) {
            $scope.$apply(function () {
//            $scope.alertMessage = ('Event Dropped to make dayDelta ' + dayDelta);
                console.log('Event Dropped dayDelta ' + dayDelta);
                console.log('Event Dropped minuteDelta ' + minuteDelta);
                console.log('Event ID: ' + event.id + ' Event Start: ' + event.start + ' Event End ' + event.end);
                saveLessonFromEvent(event);
            });
        };

        /* alert on Resize */
        $scope.eventChangeOnResize = function (event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view) {
            $scope.$apply(function () {
                console.log('Event Resized to make dayDelta ' + dayDelta);
                console.log('Event Resized to make minuteDelta ' + minuteDelta);
                console.log('Event ID: ' + event.id + ' Event Start: ' + event.start + ' Event End ' + event.end);
                saveLessonFromEvent(event);
            });
        };

        var saveLessonFromEvent = function (event) {
            var lesson = createLessonFromEvent(event);
            console.log('lesson to save from calendar: ' + JSON.stringify(lesson));
            LessonService.save(lesson);
        }

        var createLessonFromEvent = function (event) {
            var lesson = {};
            lesson.id = {_id: event.id};
            lesson.teacherId = $scope.user.id;
            lesson.start = Date.parse(event.start);
            lesson.end = Date.parse(event.end);
            lesson.studentIds = [] ;
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

        $scope.selectEvent = function (start, end, allDay) {
            var title = prompt('Titel:');
            if (title) {
                $scope.pcsCalendar.fullCalendar('renderEvent',
                    {
                        title: title,
                        start: start,
                        end: end,
                        allDay: allDay
                    },
                    true // make the event "stick"
                );
            }
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
                snapMinutes: 5, /* events can be moved in 5 minute steps, default slotMinutes */
                slotMinutes: 30, /* calender display in 30 minutes intervall, default slotMinutes = 30 minutes */
                editable: true,
                selectable: true,
                selectHelper: true,
                select: $scope.selectEvent,
                dayClick: $scope.alertDayOnClick,
                eventClick: $scope.alertEventOnClick,
                eventDrop: $scope.eventChangeOnDrop,
                eventResize: $scope.eventChangeOnResize
            }
        };

    }]);



