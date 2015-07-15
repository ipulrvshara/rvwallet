'use strict';

angular.module('rvwalletApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sample', {
                parent: 'entity',
                url: '/samples',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'rvwalletApp.sample.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sample/samples.html',
                        controller: 'SampleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sample');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sample.detail', {
                parent: 'entity',
                url: '/sample/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'rvwalletApp.sample.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sample/sample-detail.html',
                        controller: 'SampleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sample');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Sample', function($stateParams, Sample) {
                        return Sample.get({id : $stateParams.id});
                    }]
                }
            })
            .state('sample.new', {
                parent: 'sample',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sample/sample-dialog.html',
                        controller: 'SampleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('sample', null, { reload: true });
                    }, function() {
                        $state.go('sample');
                    })
                }]
            })
            .state('sample.edit', {
                parent: 'sample',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sample/sample-dialog.html',
                        controller: 'SampleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Sample', function(Sample) {
                                return Sample.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sample', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
