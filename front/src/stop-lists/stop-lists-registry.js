angular.module('stop-lists-registry', ['ui.router','commons'])
    .controller('stopListsRegistryController', function ($scope, $log, $httpService, $state) {
        $scope.stopLists = [];



        $scope.editStopList = function (stopList) {
            window.location.replace('#/stoplists/edit?stoplistId=' + stopList.id);
            // $state.go("stop-lists-edit", {id: stopList.id}); //todo routing
        };

        $scope.addStoplist = function () {
            window.location.replace('#/stoplists/add');
            // $state.go("stop-lists-add", {id: stopList.id}); //todo routing
        };

        $scope.deleteStopList = function (stoplist) {
            console.log('Deleting stop list');
            if (!stoplist){
                console.log('StopList not found');
            }
            $httpService.deleteStoplist(stoplist.id, function () {
                $log.info('Stoplist deleted successfully');
            })
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