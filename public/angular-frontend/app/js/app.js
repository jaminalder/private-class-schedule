'use strict';


// Declare app level module which depends on filters, and services
var pcs = angular.module('pcs', ['ngResource', 'ngCookies' ,'ui.calendar', 'ui.select2', '$strap']) ;

pcs.value('$strapConfig', {
    datepicker: {
        todayBtn: "linked",
        language: "de",
        todayHighlight: true
    },
    timepicker: {
        showMeridian: false,
        template: false,
        showInputs: false,
        minuteStep: 5
    }

});