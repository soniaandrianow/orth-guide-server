(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('DioceseDetailController', DioceseDetailController);

    DioceseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Diocese', 'Church'];

    function DioceseDetailController($scope, $rootScope, $stateParams, previousState, entity, Diocese, Church) {
        var vm = this;

        vm.diocese = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('orthGuideApp:dioceseUpdate', function(event, result) {
            vm.diocese = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
