'use strict';

angular.module('rvwalletApp')
    .controller('SampleDetailController', function ($scope, $rootScope, $stateParams, entity, Sample) {
        $scope.sample = entity;
        $scope.load = function (id) {
            Sample.get({id: id}, function(result) {
                $scope.sample = result;
            });
        };
        $rootScope.$on('rvwalletApp:sampleUpdate', function(event, result) {
            $scope.sample = result;
        });
    });
