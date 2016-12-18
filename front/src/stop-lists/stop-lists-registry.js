angular.module('stop-lists-registry', ['ui.router','commons'])
    .controller('stopListsRegistryController', function ($scope, $log, $httpService, $state) {
        $scope.stopLists = [];

        $httpService.findStopLists(function (result) {
            $scope.stopLists = result.data;
        });

        $scope.editStopList = function (stopList) {
            $state.go("stop-lists-edit", {id: stopList.id});
        };

        $scope.deleteStopList = function (stopList) {
            console.log('Deleting stop list');
            console.log(!!stopList ? stopList.id : 'StopList not found');
        };

    });