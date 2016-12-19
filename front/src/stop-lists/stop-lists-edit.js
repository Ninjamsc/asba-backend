angular.module('stop-lists-edit', ['ui.router', 'commons', 'angularFileUpload'])
    .controller('stopListsEditController', function ($scope, $log, $state, $stateParams,
                                                     $httpService, FileUploader) {

        $scope.stateListId = $stateParams.stoplistId;


        $httpService.getStoplist($scope.stateListId, function (result) {
            var data = result.data;
            $log.info(data);
            $scope.stoplist = data;
            if ($scope.stoplist.type !== 'bank') {
                $scope.stoplist.type = 'common'
            }
        });
        $scope.uploader = new FileUploader();

        $scope.uploadFile = function () {
            //https://github.com/nervgh/angular-file-upload/wiki/Module-API
            $log.info($scope.uploader);
            //$scope.uploader.uploadAll();
        };

    });