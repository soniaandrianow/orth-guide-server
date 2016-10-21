(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('StyleController', StyleController);

    StyleController.$inject = ['$scope', '$state', 'Style'];

    function StyleController ($scope, $state, Style) {
        var vm = this;
        
        vm.styles = [];

        loadAll();

        function loadAll() {
            Style.query(function(result) {
                vm.styles = result;
            });
        }
    }
})();
