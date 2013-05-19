'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('my app', function () {

    beforeEach(function () {
        browser().navigateTo('../../app/index.html');
    });


    it('should automatically redirect to /student when location hash/fragment is empty', function () {
        expect(browser().location().url()).toBe("/student");
    });


    describe('student', function () {

        beforeEach(function () {
            browser().navigateTo('#/student');
        });


        it('should render student list when user navigates to /student', function () {
            expect(element('#studentListTitle').text()).
                toMatch('Schülerliste');
        });

        it('should show new student form when user clicks on new student button', function () {
            element('#newStudentButton').click();
            expect(element('#studentFormTitle').text()).
                toMatch("Neuer Schüler");
        });

        it('should add a new student with firstname and lastname only.', function () {
            addStudentFirsLastName('First1', 'Last1');
            verifyStudentNameInList('First1', 'Last1', ':last');
        });

        it('should add another student. this should be appended to the list', function () {
            addStudentFirsLastName('First2', 'Last2');
            verifyStudentNameInList('First2', 'Last2', ':last');
        });

        it('should show the edit student form after clicking on the edit button of the first student', function () {
            element('#studentTable .editStudentLink :last').click();
            expect(element('#studentFormTitle').text()).
                toMatch("Schüler editieren");
            expect(input('studentForm.firstName').val()).
                toMatch('First2');
        });

        it('should change the first and last name of a student', function (){
            element('#studentTable .editStudentLink :last').click();
            input('studentForm.firstName').enter('First Changed');
            input('studentForm.lastName').enter('Last Changed');
            element('#saveStudentButton').click();
            verifyStudentNameInList('First Changed', 'Last Changed', ':last');
        });

        it('should remove the added students again after test is done', function () {
            element('#studentTable .deleteStudentLink :last').click();
            element('#studentTable .deleteStudentLink :last').click();
        });

        var addStudentFirsLastName = function (firstname, lastname) {
            element('#newStudentButton').click();
            input('studentForm.firstName').enter(firstname);
            input('studentForm.lastName').enter(lastname);
            element('#saveStudentButton').click();
        };

        var verifyStudentNameInList = function (firstname, lastname, positionInList) {
            expect(element('#studentTable .studentListName' + positionInList).text()).
                toMatch(firstname + ' ' + lastname)
        }


    });


});
