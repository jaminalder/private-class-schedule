'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('my app', function () {

    beforeEach(function () {
        browser().navigateTo('../../app/index.html');
    });


    it('should automatically redirect to / = home when location hash/fragment is empty', function () {
        expect(browser().location().url()).toBe("/home");
    });


    describe('student', function () {

        beforeEach(function () {
            browser().navigateTo('#/login');
            input('loginForm.eMail').enter('test@user.com');
            input('loginForm.password').enter('12345');
            element('#loginButton').click();
            sleep(1);
        });

        it('register test user first', function () {
            browser().navigateTo('#/register');
            input('registerForm.user.firstName').enter('Test');
            input('registerForm.user.lastName').enter('User');
            input('registerForm.user.eMail').enter('test@user.com');
            input('registerForm.password').enter('12345');
            element('#registerButton').click();
        });

        it('should show calendar with student list per default', function () {
            expect(element('#studentListTitle').text()).
                toMatch('Schülerliste');
        });

        it('should show new student form when user clicks on new student button', function () {
            element('#newStudentButton').click();
            sleep(1);
            expect(element('#studentFormTitle').text()).
                toMatch("Neuer Schüler");
        });

        it('should add a new student with firstname and lastname only.', function () {
            addStudentFirstLastName('First1', 'Last1');
            verifyStudentNameInList('First1', 'Last1', ':last');
        });

        it('should add another student. this should be appended to the list', function () {
            addStudentFirstLastName('First2', 'Last2');
            verifyStudentNameInList('First2', 'Last2', ':last');
        });

        it('should show the edit student form after clicking on the edit button of the last student', function () {
            element('#studentTable .editStudentLink :last').click();
            sleep(1);
            expect(element('#studentFormTitle').text()).
                toMatch("Schüler editieren");
            expect(input('studentForm.firstName').val()).
                toMatch('First2');
        });

        it('should change the first and last name of a student', function (){
            element('#studentTable .editStudentLink :last').click();
            sleep(1);
            input('studentForm.firstName').enter('First Changed');
            input('studentForm.lastName').enter('Last Changed');
            element('#saveStudentButton').click();
            sleep(1);
            verifyStudentNameInList('First Changed', 'Last Changed', ':last');
        });

        it('should remove the added students again after test is done', function () {
            element('#studentTable .deleteStudentLink :last').click();
            sleep(1);
            element('#studentTable .deleteStudentLink :last').click();
            sleep(1);
        });

        var addStudentFirstLastName = function (firstname, lastname) {
            element('#newStudentButton').click();
            sleep(1);
            input('studentForm.firstName').enter(firstname);
            input('studentForm.lastName').enter(lastname);
            element('#saveStudentButton').click();
            sleep(1);
        };

        var verifyStudentNameInList = function (firstname, lastname, positionInList) {
            expect(element('#studentTable .studentListName' + positionInList).text()).
                toMatch(firstname + ' ' + lastname);
        };


    });

    describe('lesson', function () {

        beforeEach(function () {
            browser().navigateTo('#/login');
            input('loginForm.eMail').enter('test@user.com');
            input('loginForm.password').enter('12345');
            element('#loginButton').click();
            sleep(1);
        });

        it('register test user first', function () {
            browser().navigateTo('#/register');
            input('registerForm.user.firstName').enter('Test');
            input('registerForm.user.lastName').enter('User');
            input('registerForm.user.eMail').enter('test@user.com');
            input('registerForm.password').enter('12345');
            element('#registerButton').click();
        });

        it('should render lesson list when user clicks on tab lessonList', function () {
            element('#lessonListTab').click();
            sleep(1);
            expect(element('#lessonListTitle').text()).
                toMatch('Lektionenliste');
        });

        it('should show new lesson form when user clicks on new lesson button', function () {
            element('#lessonListTab').click();
            sleep(1);
            element('#newLessonButton').click();
            sleep(1);
            expect(element('#lessonFormTitle').text()).
                toMatch("Neue Lektion");
        });

        it('should add a new lesson with date, start and end time only.', function () {
            addLessonDateTime('11.07.2013', '10:00' , '11:30');
            verifyLessonDateTimeInList('11.07.2013', '10:00', '11:30', ':last');
        });

        it('should add another lesson. this should be appended to the list', function () {
            addLessonDateTime('12.07.2013', '14:00' , '16:00');
            verifyLessonDateTimeInList('12.07.2013', '14:00', '16:00', ':last');
        });

        it('should show the edit lesson form after clicking on the edit button of the last lesson', function () {
            element('#lessonListTab').click();
            sleep(1);
            element('#lessonTable .editLessonLink :last').click();
            sleep(1);
            expect(element('#lessonFormTitle').text()).
                toMatch("Lektion editieren");
            expect(element('#lessonTable .lessonStart').text()).
                toMatch('12.07.2013 14:00');
        });

        it('should change the start and end time of a lesson', function (){
            element('#lessonListTab').click();
            sleep(1);
            element('#lessonTable .editLessonLink :last').click();
            sleep(1);
            input('lessonForm.startTime').enter('08:15');
            input('lessonForm.endTime').enter('09:05');
            element('#saveLessonButton').click();
            sleep(1);
            verifyLessonDateTimeInList('12.07.2013', '08:15', '09:05', ':last');
        });

        it('should remove the added lessons again after test is done', function () {
            element('#lessonListTab').click();
            sleep(1);
            element('#lessonTable .deleteLessonLink :last').click();
            sleep(1);
            element('#lessonTable .deleteLessonLink :last').click();
            sleep(1);
        });

        var addLessonDateTime = function (lessondate, starttime, endtime) {
            element('#lessonListTab').click();
            sleep(1);
            element('#newLessonButton').click();
            sleep(1);
            input('lessonForm.date').enter(lessondate);
            input('lessonForm.startTime').enter(starttime);
            input('lessonForm.endTime').enter(endtime);
            element('#saveLessonButton').click();
            sleep(1);
        };

        var verifyLessonDateTimeInList = function (lessondate, starttime, endtime, positionInList) {
            element('#lessonListTab').click();
            expect(element('#lessonTable .lessonStart' + positionInList).text()).
                toMatch(lessondate + ' ' + starttime);
            expect(element('#lessonTable .lessonEnd' + positionInList).text()).
                toMatch(lessondate + ' ' + endtime);
        };


    });


});
