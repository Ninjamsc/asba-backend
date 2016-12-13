angular.module('stop-lists-edit', ['ui.router','commons'])
    .controller('stopListsEditController', function ($scope, $log, $state, $stateParams) {
        
        $scope.stateListId = $stateParams.id; //todo stateParams

        console.log($stateParams);

        /*$httpService.findStopLists(function (result) {
            $scope.stopLists = result.data;
        });*/
    });