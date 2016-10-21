(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('ChurchDetailController', ChurchDetailController);

    ChurchDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Church', 'Diocese', 'Style'];

    function ChurchDetailController($scope, $rootScope, $stateParams, previousState, entity, Church, Diocese, Style) {
        var vm = this;

        vm.church = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('orthGuideApp:churchUpdate', function(event, result) {
            vm.church = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
