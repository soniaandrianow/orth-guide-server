(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('ChurchDialogController', ChurchDialogController);

    ChurchDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Church', 'Diocese', 'Style'];

    function ChurchDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Church, Diocese, Style) {
        var vm = this;

        vm.church = entity;
        vm.clear = clear;
        vm.save = save;
        vm.diocese = Diocese.query();
        vm.styles = Style.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.church.id !== null) {
                Church.update(vm.church, onSaveSuccess, onSaveError);
            } else {
                Church.save(vm.church, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('orthGuideApp:churchUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
