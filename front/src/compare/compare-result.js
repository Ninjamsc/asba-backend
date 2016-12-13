angular.module('compare-result-view', ['ui.router', 'commons'])
    .controller('compareResultViewController', function ($scope, $log, $state, $stateParams,$httpService) {



        $scope.findRequest = function(){
            $log.info($scope.creditRequestIdForSearch);
            $httpService.findCompareResult($scope.creditRequestIdForSearch, function (result) {
                $log.info("success");
                $log.info(result.data);
                $scope.compareResult = result.data;
            });
        };



        //todo remove
        $scope.creditRequestIdForSearch = 15;
        $scope.findRequest();
    });