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
                // result.data.rules = []; //todo remove
                result.data.biometricCoreResponse = { //todo remove
                    "creditRequestId": "113456",
                    "clientId": "cl 100500",
                    "operatorId": "op 13",
                    "requestDate": "12:45 13.56.2016",
                    "responseDate": "13:56 12.45.2016"
                };
                $scope.compareResult = result.data;
            });
        };


        //todo remove
        $scope.creditRequestIdForSearch = 'compare-result-view.json';
        $scope.findRequest();
    });