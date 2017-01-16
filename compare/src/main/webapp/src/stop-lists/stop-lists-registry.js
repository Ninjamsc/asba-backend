angular.module('stop-lists-registry', ['ui.router','commons'])
    .controller('stopListsRegistryController', function ($rootScope,$scope, $log, $httpService, $state, $stateParams) {
        if(!!$stateParams.auth) {
            $httpService.userInfo(function (user){
                $rootScope.user = user;
            });
        }
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
                $scope.refresh();
            })
        };

        $scope.getOwnerId = function (stopList) {
            if (!!stopList && !!stopList.owner && stopList.owner.length > 0){
                return stopList.owner[0].id;
            }
        };


         $scope.refresh = function() {
            $httpService.findStopLists(function (result) {
                $scope.stopLists = result.data;
            });
        };

        //Инициализация
        $scope.refresh();

        $scope.login = function() {
            window.location.replace('#/login');
        }
    });