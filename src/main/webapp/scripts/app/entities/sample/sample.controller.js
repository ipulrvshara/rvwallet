'use strict';

angular.module('rvwalletApp')
    .controller('SampleController', function ($scope, Sample, ParseLinks) {
        $scope.samples = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Sample.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.samples = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Sample.get({id: id}, function(result) {
                $scope.sample = result;
                $('#deleteSampleConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Sample.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSampleConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.sample = {name: null, description: null, id: null};
        };
    });
