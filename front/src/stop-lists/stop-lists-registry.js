angular.module('stop-lists-registry', ['ui.router','commons'])
    .controller('stopListsRegistryController', function ($scope, $log, $httpService, $state) {
        $scope.stopLists = [];

        

        $scope.editStopList = function (stopList) {
            window.open('#/stoplists/edit?stoplistId=' + stopList.id, '');
            // $state.go("stop-lists-edit", {id: stopList.id}); //todo routing
        };

        $scope.deleteStopList = function (stopList) {
            console.log('Deleting stop list');
            console.log(!!stopList ? stopList.id : 'StopList not found');
        };

        $scope.getOwnerId = function (stopList) {
            if (!!stopList && !!stopList.owner && stopList.owner.length > 0){
                return stopList.owner[0].id;
            }
        };

        //Инициализация
        $httpService.findStopLists(function (result) {
            $scope.stopLists = result.data;
        });


    });