(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('church', {
            parent: 'entity',
            url: '/church',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Churches'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/church/churches.html',
                    controller: 'ChurchController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('church-detail', {
            parent: 'entity',
            url: '/church/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Church'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/church/church-detail.html',
                    controller: 'ChurchDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Church', function($stateParams, Church) {
                    return Church.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'church',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('church-detail.edit', {
            parent: 'church-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/church/church-dialog.html',
                    controller: 'ChurchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Church', function(Church) {
                            return Church.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('church.new', {
            parent: 'church',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/church/church-dialog.html',
                    controller: 'ChurchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                address: null,
                                longitude: null,
                                latitude: null,
                                fete: null,
                                dedication: null,
                                parson: null,
                                century: null,
                                wooden: false,
                                services: null,
                                short_history: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('church', null, { reload: 'church' });
                }, function() {
                    $state.go('church');
                });
            }]
        })
        .state('church.edit', {
            parent: 'church',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/church/church-dialog.html',
                    controller: 'ChurchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Church', function(Church) {
                            return Church.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('church', null, { reload: 'church' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('church.delete', {
            parent: 'church',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/church/church-delete-dialog.html',
                    controller: 'ChurchDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Church', function(Church) {
                            return Church.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('church', null, { reload: 'church' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
