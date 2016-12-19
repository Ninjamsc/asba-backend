angular.module('stop-lists-edit', ['ui.router', 'commons', 'angularFileUpload'])
    .controller('stopListsEditController', function ($scope, $log, $state, $stateParams,
                                                     $httpService, FileUploader) {

        $scope.stoplistId = $stateParams.stoplistId;

        $log.info($scope.stoplistId);
        $scope.refresh = function () {
            if (!!$scope.stoplistId) {//Редактирование
                $httpService.getStoplist($scope.stoplistId, function (result) {
                    var data = result.data;
                    if (!!data) {
                        $scope.stoplist = data;
                        if ($scope.stoplist.type !== 'bank') {
                            $scope.stoplist.type = 'common'
                        }
                    }
                    else {
                        // $scope.stoplistNotFound = true;
                        $log.info('StopList not found');
                    }
                });
            } else { //Создание
                $scope.stoplist = {};
            }
        };

        $scope.zipUploader = new FileUploader();
        $scope.zipUploader.url = '/rpe/api/stop-list/' + $scope.stoplistId + '/upload';
        $scope.zipUploader.onSuccessItem = function (fileItem, response, status, headers) {
            $log.info('Success upload', response);
            $scope.refresh();
        };

        $scope.uploadZip = function () {
            //https://github.com/nervgh/angular-file-upload/wiki/Module-API
            $log.info($scope.zipUploader);
            $log.info($scope.zipUploader.queue[0]);
            $scope.zipUploader.queue[0].upload();
        };

        $scope.photoUploader = new FileUploader();
        $scope.photoUploader.url = '/rpe/api/stop-list/' + $scope.stoplistId + '/upload';
        $scope.photoUploader.onSuccessItem = function (fileItem, response, status, headers) {
            $log.info('Success upload', response);
            $scope.refresh();
        };

        $scope.uploadPhoto = function () {
            //https://github.com/nervgh/angular-file-upload/wiki/Module-API
            $log.info($scope.photoUploader);
            $log.info($scope.photoUploader.queue[0]);
            $scope.photoUploader.queue[0].upload();
        };

        $scope.deletePerson = function (person) {
            $log.info("delete person", data);
        };

        $scope.saveStoplist = function () {
            $log.info($scope.stoplist);
            if (!!$scope.stoplist.id) {
                $httpService.editStoplist($scope.stoplist, function (data) {
                    $log.info("success update", data);
                });
            } else {
                $httpService.addStoplist($scope.stoplist, function (data) {
                    $log.info("success addition", data);
                    var stoplistId = data.data;
                    window.location.replace('#/stoplists/edit?stoplistId=' + stoplistId);
                });
            }
        };


        $scope.refresh();

    });