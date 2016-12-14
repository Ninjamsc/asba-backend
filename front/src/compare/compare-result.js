angular.module('compare-result-view', ['ui.router', 'commons'])
    .controller('compareResultViewController', function ($scope, $log, $state, $stateParams, $httpService) {


        $scope.findRequest = function () {
            $log.info($scope.creditRequestIdForSearch);
            $httpService.findCompareResult($scope.creditRequestIdForSearch, function (result) {
                console.log(result.data);
                var data = result.data;
                var res = {};

                res.rules = data.rules;
                res.scannedPicture = {
                    pictureURL: data.scannedPicture.pictureURL,
                    previewURL: data.scannedPicture.previewURL
                };
                res.cameraPicture = {
                    pictureURL: data.cameraPicture.pictureURL,
                    previewURL: data.cameraPicture.previewURL
                };
                $scope.compareResult = res;
            });
        };


        //todo remove
        $scope.creditRequestIdForSearch = 15;
        $scope.findRequest();
    });