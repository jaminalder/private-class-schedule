'use strict';

angular.module('pcs')
    .factory('DateTimeService', function () {

        var service = {
            getDateMidnight: function (date) {
                if (!(date instanceof Date)) {
                    date = new Date(date);
                }
                var result = new Date(date);
                result.setUTCHours(0, 0, 0, 0);
                return result;
            },
            getTimeString: function (date) {
                if (!(date instanceof Date)) {
                    date = new Date(date);
                }
                var h = date.getHours() > 9 ? date.getHours() : "0" + date.getHours();
                var m = date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes();
                return h + ":" + m;
            },
            getMillisFromTimeString: function (date, timeString) {
                var result = new Date(date);
                var timeSplit = timeString.split(":");
                var hours = timeSplit[0];
                var minutes = timeSplit[1];
                result.setHours(hours);
                result.setMinutes(minutes);
                return result.getTime();
            }
        };

        return service;

    });
