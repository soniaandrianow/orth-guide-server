(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('ChurchDeleteController',ChurchDeleteController);

    ChurchDeleteController.$inject = ['$uibModalInstance', 'entity', 'Church'];

    function ChurchDeleteController($uibModalInstance, entity, Church) {
        var vm = this;

        vm.church = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Church.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
