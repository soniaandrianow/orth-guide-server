(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('StyleDetailController', StyleDetailController);

    StyleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Style', 'Church'];

    function StyleDetailController($scope, $rootScope, $stateParams, previousState, entity, Style, Church) {
        var vm = this;

        vm.style = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('orthGuideApp:styleUpdate', function(event, result) {
            vm.style = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
