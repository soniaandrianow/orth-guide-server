(function() {
    'use strict';

    angular
        .module('orthGuideApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('style', {
            parent: 'entity',
            url: '/style',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Styles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/style/styles.html',
                    controller: 'StyleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('style-detail', {
            parent: 'entity',
            url: '/style/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Style'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/style/style-detail.html',
                    controller: 'StyleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Style', function($stateParams, Style) {
                    return Style.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'style',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('style-detail.edit', {
            parent: 'style-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/style/style-dialog.html',
                    controller: 'StyleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Style', function(Style) {
                            return Style.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('style.new', {
            parent: 'style',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/style/style-dialog.html',
                    controller: 'StyleDialogController',
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
                    $state.go('style', null, { reload: 'style' });
                }, function() {
                    $state.go('style');
                });
            }]
        })
        .state('style.edit', {
            parent: 'style',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/style/style-dialog.html',
                    controller: 'StyleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Style', function(Style) {
                            return Style.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('style', null, { reload: 'style' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('style.delete', {
            parent: 'style',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/style/style-delete-dialog.html',
                    controller: 'StyleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Style', function(Style) {
                            return Style.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('style', null, { reload: 'style' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
