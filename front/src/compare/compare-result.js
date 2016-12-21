angular.module('compare-result-view', ['ui.router', 'commons'])
    .controller('compareResultViewController', function ($scope, $log, $state, $stateParams,
                                                         $httpService, $c,
                                                         RuleErrorType) {
        console.log($stateParams);



        $scope.findRequest = function () {
            $log.info("$scope.findRequest requestId =", $scope.creditRequestIdForSearch);
            $httpService.findCompareResult($scope.creditRequestIdForSearch, function (result) {
                var data = result.data;
                // console.log(data);
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
                $scope.compareResult = result;

                if (!!$scope.compareResult.similarPictures &&
                    !!$scope.compareResult.similarPictures.photos &&
                    !!$scope.compareResult.similarPictures.photos.length > 0){
                    $scope.similarPicturesMatrix = $c.arrayToMatrix($scope.compareResult.similarPictures.photos, 5);
                    console.log($scope.similarPicturesMatrix);
                }

                if (!!$scope.compareResult.othernessPictures &&
                if (!!$scope.compareResult.othernessPictures.photos &&
                    !!$scope.compareResult.othernessPictures.length > 0){
                    $scope.othernessPicturesMatrix = $c.arrayToMatrix($scope.compareResult.othernessPictures.photos, 5);
                    console.log($scope.othernessPicturesMatrix);
                }
            });
        };

        $scope.onSearch = function(){
            $scope.searchInitialized = true;
            $scope.findRequest();
        };

        $scope.onSearchTextFieldKeydown = function ($event) {
            if ($event.which === 13 || $event.which === 32) {
                $scope.findRequest();
            }
        };

        $scope.getRuleName = function (ruleId) {
            for (var rule in RuleErrorType) {
                if (RuleErrorType.hasOwnProperty(rule)) {
                    if (RuleErrorType[rule].ID == ruleId) {
                        return RuleErrorType[rule].NAME;
                    }
                }
            }
        };

        
        //Инициализация 
        if ($stateParams.requestId) {
            $scope.creditRequestIdForSearch = $stateParams.requestId;
            $scope.findRequest();
        }

    });