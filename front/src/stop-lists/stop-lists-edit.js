angular.module('stop-lists-edit', ['ui.router', 'commons', 'angularFileUpload'])
    .controller('stopListsEditController', function ($scope, $log, $state, $stateParams,
                                                     $httpService, FileUploader) {

        $scope.stateListId = $stateParams.stoplistId;

        $log.info($scope.stateListId);
        $scope.refresh = function () {
            if (!!$scope.stateListId) {
                $httpService.getStoplist($scope.stateListId, function (result) {
                    var data = result.data;
                    if (!!data) {
                        $scope.stoplist = data;
                        if ($scope.stoplist.type !== 'bank') {
                            $scope.stoplist.type = 'common'
                        }
                    }
                    else {
                        $scope.stoplistNotFound = true;
                        $log.info('StopList not found');
                    }
                });
            }
        };

        $scope.uploader = new FileUploader();
        $scope.uploader.url = '/rpe/api/stop-list/' + $scope.stateListId + '/upload';
        $scope.uploader.onSuccessItem = function (fileItem, response, status, headers) {
            $log.info('Success upload', response);
            $scope.refresh();
        };

        $scope.uploadFile = function () {
            //https://github.com/nervgh/angular-file-upload/wiki/Module-API
            $log.info($scope.uploader);
            $log.info($scope.uploader.queue[0]);
            $scope.uploader.queue[0].upload();
        };

        $scope.saveStoplist = function () {
            $httpService.editStoplist($scope.stoplist, function (data) {
                $log.info("success save", data);
            });
        };


        $scope.refresh();

    });