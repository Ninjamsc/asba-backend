angular.module('compare-result-view', ['ui.router', 'commons'])
    .controller('compareResultViewController', function ($scope, $log, $state, $stateParams, $httpService) {


        $scope.onSearchTextFieldKeydown = function ($event) {
            if ($event.which === 13 || $event.which === 32) {
                findRequest();
            }
        };

        $scope.findRequest = function () {
            $log.info($scope.creditRequestIdForSearch);
            $httpService.findCompareResult($scope.creditRequestIdForSearch, function (result) {
                console.log(result.data);

                $scope.compareResult = result.data;
            });
        };


        //todo remove
        $scope.creditRequestIdForSearch = 'compare-result-view.json';
        $scope.findRequest();
    });