(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('DioceseDeleteController',DioceseDeleteController);

    DioceseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Diocese'];

    function DioceseDeleteController($uibModalInstance, entity, Diocese) {
        var vm = this;

        vm.diocese = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Diocese.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
