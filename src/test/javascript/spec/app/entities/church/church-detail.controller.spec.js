'use strict';

describe('Controller Tests', function() {

    describe('Church Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChurch, MockDiocese, MockStyle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChurch = jasmine.createSpy('MockChurch');
            MockDiocese = jasmine.createSpy('MockDiocese');
            MockStyle = jasmine.createSpy('MockStyle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Church': MockChurch,
                'Diocese': MockDiocese,
                'Style': MockStyle
            };
            createController = function() {
                $injector.get('$controller')("ChurchDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'orthGuideApp:churchUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
