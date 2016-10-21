(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .controller('StyleDialogController', StyleDialogController);

    StyleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Style', 'Church'];

    function StyleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Style, Church) {
        var vm = this;

        vm.style = entity;
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
            if (vm.style.id !== null) {
                Style.update(vm.style, onSaveSuccess, onSaveError);
            } else {
                Style.save(vm.style, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('orthGuideApp:styleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
