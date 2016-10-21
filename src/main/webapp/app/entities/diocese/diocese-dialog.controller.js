(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('DioceseDialogController', DioceseDialogController);

    DioceseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Diocese', 'Church'];

    function DioceseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Diocese, Church) {
        var vm = this;

        vm.diocese = entity;
        vm.clear = clear;
        vm.save = save;
        vm.churches = Church.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.diocese.id !== null) {
                Diocese.update(vm.diocese, onSaveSuccess, onSaveError);
            } else {
                Diocese.save(vm.diocese, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('orthGuideApp:dioceseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
