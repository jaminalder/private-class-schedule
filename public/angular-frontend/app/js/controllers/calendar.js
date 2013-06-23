/**
 * calendarDemoApp - 0.1.3
 * https://github.com/angular-ui/ui-calendar
 * MIT License
 */

'use strict';

angular.module('pcs');

function CalendarCtrl($scope, $filter, UserService, LessonService) {
    var User = UserService;
    var Lesson = LessonService;
    /* empty array lessonEvent will be used to get all lessons for the logged in teacher */
    $scope.lessonEvents = [];

    User.get(function (userResult) {
        console.log('user result: ' + JSON.stringify(userResult));
        $scope.user = userResult;
        $scope.lessons = Lesson.allLessonsOfTeacher({'teacherId': userResult.id._id},function(lessonResult){
            var i
            for (i in $scope.lessons) {
            var lessonId = lessonResult[i].id;
            var lessonStart =  lessonResult[i].start / 1000;
            var lessonEnd = lessonResult[i].end / 1000;
            $scope.lessonEvents.push(
                {id: lessonId, title: 'Lesson ' + i ,start: lessonStart ,end: lessonEnd ,allDay: false, className: ['customFeed']});
            }
            $scope.eventSources.push($scope.lessonEvents);
        });
    });

    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();

    /* event source that pulls from google.com */
    $scope.eventSource = {
 //       url: "http://www.google.com/calendar/feeds/usa__en%40holiday.calendar.google.com/public/basic",
 //       className: 'gcal-event', // an option!
        currentTimezone: 'America/Chicago' // an option!
    };


    /* event source that contains custom events on the scope */
    $scope.events = [
/*        {title: 'All Day Event',start: new Date(y, m, 1)},
        {title: 'Long Event',start: new Date(y, m, d - 5),end: new Date(y, m, d - 2)},
        {id: 999,title: 'Repeating Event',start: new Date(y, m, d - 3, 16, 0),allDay: false},
        {id: 999,title: 'Repeating Event',start: new Date(y, m, d + 4, 16, 0),allDay: false},
        {title: 'Birthday Party',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30),allDay: false},
        {title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'}
*/
    ];
    /* event source that calls a function on every view switch */
    /* funktioniert, erwartet aber eine Zeit in Sekunden, nicht milliseconds */

    $scope.eventsF = function (start, end, callback) {
        var s = new Date(start).getTime() / 1000;
        var e = new Date(end).getTime() / 1000;
        var m = new Date(start).getMonth();
        // start 1371024000000, end 1371031200000
//        var events = [{title: 'Feed Me ' + m,start: 1371024000000 / 1000 ,end: 1371031200000 / 1000 ,allDay: false, className: ['customFeed']}];
        var events = [{title: 'Feed Me ' + m,start: s + (50000),end: s + (100000),allDay: false, className: ['customFeed']}];
        callback(events);
    };
    /* alert on dayClick */
    $scope.alertDayOnClick = function( date, allDay, jsEvent, view ){
        $scope.$apply(function(){
            console.log('Day Clicked ' + date);
            $scope.alertMessage = ('Day Clicked ' + date);
        });
    };
    /* alert on eventClick */
    $scope.alertEventOnClick = function( event, jsEvent, view ){
        $scope.$apply(function(){
            console.log('Event Clicked ' + event);
            $scope.alertMessage = ('Event Clicked ' + event);
        });
    };
    /* alert on Drop */
    $scope.alertOnDrop = function(event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view){
        $scope.$apply(function(){
            $scope.alertMessage = ('Event Droped to make dayDelta ' + dayDelta);
        });
    };
    /* alert on Resize */
    $scope.alertOnResize = function(event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ){
        $scope.$apply(function(){
            $scope.alertMessage = ('Event Resized to make dayDelta ' + minuteDelta);
        });
    };
    /* add and removes an event source of choice */
    $scope.addRemoveEventSource = function(sources,source) {
        var canAdd = 0;
        angular.forEach(sources,function(value, key){
            if(sources[key] === source){
                sources.splice(key,1);
                canAdd = 1;
            }
        });
        if(canAdd === 0){
            sources.push(source);
        }
    };
    /* add custom event*/
    $scope.addEvent = function() {
        $scope.events.push({
            title: 'Open Sesame',
            start: new Date(y, m, 28),
            end: new Date(y, m, 29),
            className: ['openSesame']
        });
    };
    /* remove event */
    $scope.remove = function(index) {
        $scope.events.splice(index,1);
    };
    /* Change View */
    $scope.changeView = function(view) {
        $scope.pcsCalendar.fullCalendar('changeView',view);
    };
    /* next year for day, week, month display */
    $scope.nextYear = function() {
        $scope.pcsCalendar.fullCalendar('nextYear');
    };
    /* previous year for day, week, month display */
    $scope.prevYear = function() {
        $scope.pcsCalendar.fullCalendar('prevYear');
    };
    /* next day, week, month display */
    $scope.next = function() {
        $scope.pcsCalendar.fullCalendar('next');
    };
    /* previous day, week, month display */
    $scope.prev = function() {
        $scope.pcsCalendar.fullCalendar('prev');
    };
    /* display today for current view day, week or month*/
    $scope.today = function() {
        $scope.pcsCalendar.fullCalendar('today');
    };
    $scope.selectEvent = function(start, end, allDay) {
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
        calendar:{
            height: 600,    /* height of calendar */
            header: {
                left:   'today,prevYear,prev,next,nextYear',
                center: 'title',
                right:  'agendaDay,agendaWeek,month'
 /*             to create a separate line with buttons in the html view, the buttons on the header can be deactivated
                left: 'deactivateButton',
                center: 'title'     ,
                right: 'deactivateButton'    */
            },
            buttonText: {
                today:      'Heute',
                prevYear:   '&lt;&lt;',
                prev:       '&lt;',
                next:       '&gt;',
                nextYear:   '&gt;&gt;',
                agendaDay:  'Tag',
                agendaWeek: 'Woche',
                month:      'Monat'
            },
            theme: false,
            monthNames: ['Januar', 'Februar', 'März', 'April', 'Mai', 'Juni', 'Juli',
                'August', 'September', 'Oktober', 'November', 'Dezember']    ,
            monthNamesShort: ['Jan', 'Feb', 'Mrz', 'Apr', 'Mai', 'Jun', 'Jul',
                'Aug', 'Sep', 'Okt', 'Nov', 'Dez']    ,
            dayNames: ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch',
                'Donnerstag', 'Freitag', 'Samstag'],
            dayNamesShort: ['So', 'Mo', 'Di', 'Mi',
                'Do', 'Fr', 'Sa'] ,
            titleFormat: {
                month: 'MMMM yyyy',                                 // September 2009
                week: "d. [MMMM] [ yyyy]{ '&#8212;' d. MMMM yyyy}", // 7. - 13. September 2009
                day: 'dddd, d. MMMM yyyy'                           // Dienstag, 8. September 2009
            },
            columnFormat: {
                month: 'ddd',    // Mo
                week: 'ddd, d.M.', // Mo, 7.9.
                day: 'dddd, d.M.'  // Montag, 7.9.
            } ,
            allDayText: 'Ganzt.',
            axisFormat: 'H:mm',
            firstDay: 1,
            snapMinutes: 5, /* events can be moved in 5 minute steps, default slotMinutes */
            slotMinutes: 30 ,  /* calender display in 30 minutes intervall, default slotMinutes = 30 minutes */
            editable: true,
            selectable: true,
            selectHelper: true,
            select: $scope.selectEvent,
            dayClick: $scope.alertDayOnClick,
            eventClick: $scope.alertEventOnClick,
            eventDrop: $scope.alertOnDrop,
            eventResize: $scope.alertOnResize
        }
    };
    /* event sources array*/
    $scope.eventSources = [$scope.events, $scope.eventSource, $scope.eventsF];
}
/* EOF */



