(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('DioceseController', DioceseController);

    DioceseController.$inject = ['$scope', '$state', 'Diocese'];

    function DioceseController ($scope, $state, Diocese) {
        var vm = this;
        
        vm.diocese = [];

        loadAll();

        function loadAll() {
            Diocese.query(function(result) {
                vm.diocese = result;
            });
        }
    }
})();
