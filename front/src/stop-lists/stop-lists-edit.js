angular.module('stop-lists-edit', ['ui.router', 'commons'])
    .controller('stopListsEditController', function ($scope, $log, $state, $stateParams, $httpService) {

        $scope.stateListId = $stateParams.stoplistId;


        $httpService.getStoplist($scope.stateListId, function (result) {
                $log.info(result.data);
        });
    });