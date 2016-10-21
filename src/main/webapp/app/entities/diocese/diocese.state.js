(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('diocese', {
            parent: 'entity',
            url: '/diocese',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Diocese'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diocese/diocese.html',
                    controller: 'DioceseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('diocese-detail', {
            parent: 'entity',
            url: '/diocese/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Diocese'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diocese/diocese-detail.html',
                    controller: 'DioceseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Diocese', function($stateParams, Diocese) {
                    return Diocese.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'diocese',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('diocese-detail.edit', {
            parent: 'diocese-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diocese/diocese-dialog.html',
                    controller: 'DioceseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Diocese', function(Diocese) {
                            return Diocese.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diocese.new', {
            parent: 'diocese',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diocese/diocese-dialog.html',
                    controller: 'DioceseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('diocese', null, { reload: 'diocese' });
                }, function() {
                    $state.go('diocese');
                });
            }]
        })
        .state('diocese.edit', {
            parent: 'diocese',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diocese/diocese-dialog.html',
                    controller: 'DioceseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Diocese', function(Diocese) {
                            return Diocese.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('diocese', null, { reload: 'diocese' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diocese.delete', {
            parent: 'diocese',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diocese/diocese-delete-dialog.html',
                    controller: 'DioceseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Diocese', function(Diocese) {
                            return Diocese.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('diocese', null, { reload: 'diocese' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
