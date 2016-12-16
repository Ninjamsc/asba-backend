angular.module('compare-result-view', ['ui.router', 'commons'])
    .controller('compareResultViewController', function ($scope, $log, $state, $stateParams, $httpService) {
        console.log($stateParams);

        $scope.findRequest = function () {
            $log.info($scope.creditRequestIdForSearch);
            $httpService.findCompareResult($scope.creditRequestIdForSearch, function (result) {
                var data = result.data;
                console.log(data);
                //data.rules = []; //todo remove

                var result = data;
                result.biometricCoreResponse = { //todo remove
                    "creditRequestId": data.wfNumber,
                    "clientId": data.IIN,
                    "operatorId": data.username,
                    "requestDate": data.timestamp,
                    "responseDate": data["created-at"]
                };
                result.cameraPicture.pictureURL = data.webCamPictureURL;
                result.cameraPicture.previewURL = data.webCamPicturePreviewURL;
                result.scannedPicture.pictureURL = data.scannedPictureURL;
                result.scannedPicture.previewURL = data.scannedPicturePreviewURL;
                /*"wfNumber" : 11154,
                 "IIN" : 11154,
                 "username" : "OperatorName",
                 "timestamp" : "12.12.16.12.00.12",
                 "scannedPictureURL" : "http://www.sdorohov.ru/storage/rest/image/MzlmYzM2YTEtNDdlMy00NjIzLThiMTYtNGZmNTg4M2RlYmVi.jpg",
                 "scannedPicturePreviewURL" : null,
                 "webCamPictureURL" : "http://www.sdorohov.ru/storage/rest/image/YWNjMDdlZmEtMTJjMS00MTM3LWIwNGYtOTBmMWIzMjIyZDdm.jpg",
                 "webCamPicturePreviewURL" : null*/
                $scope.compareResult = result;

            });

            if($stateParams.requestId) {
                $scope.creditRequestIdForSearch = $stateParams.requestId;
                $scope.findRequest();
            }
        };

        $scope.onSearchTextFieldKeydown = function ($event) {
            if ($event.which === 13 || $event.which === 32) {
                $scope.findRequest();
            }
        };


        //todo remove
        // $scope.creditRequestIdForSearch = 'compare-result-view.json';
        // $scope.findRequest();
    });