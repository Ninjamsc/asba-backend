angular.module('stop-lists-edit', ['ui.router', 'commons'])
    .controller('stopListsEditController', function ($scope, $log, $state, $stateParams, $httpService) {

        $scope.stateListId = $stateParams.stoplistId;


        $httpService.getStoplist($scope.stateListId, function (result) {
            var data = result.data;
            $log.info(data);
                $scope.stoplist = data;
                if ($scope.stoplist.type !== 'bank'){
                    $scope.stoplist.type = 'common'
                }
        });
    });