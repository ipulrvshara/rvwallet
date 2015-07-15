'use strict';

angular.module('rvwalletApp').controller('SampleDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Sample',
        function($scope, $stateParams, $modalInstance, entity, Sample) {

        $scope.sample = entity;
        $scope.load = function(id) {
            Sample.get({id : id}, function(result) {
                $scope.sample = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('rvwalletApp:sampleUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.sample.id != null) {
                Sample.update($scope.sample, onSaveFinished);
            } else {
                Sample.save($scope.sample, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
