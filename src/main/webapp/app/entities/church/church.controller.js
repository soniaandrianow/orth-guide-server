(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('ChurchController', ChurchController);

    ChurchController.$inject = ['$scope', '$state', 'Church'];

    function ChurchController ($scope, $state, Church) {
        var vm = this;
        
        vm.churches = [];

        loadAll();

        function loadAll() {
            Church.query(function(result) {
                vm.churches = result;
            });
        }
    }
})();
