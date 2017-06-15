angular.module('stop-lists-registry', ['ui.router', 'commons'])

    .controller('stopListsRegistryController', function ($rootScope, $scope, $log, $httpService, $state, $stateParams, $http) {
        if (!!$stateParams.auth) {
            $httpService.userInfo(function (user) {
                $rootScope.user = user.data;
                console.log($rootScope.user);
                console.log('$rootScope.user = ' + $rootScope.user);
                console.log('$rootScope.user.admin = ' + $rootScope.user.admin);
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
            if (!stoplist) {
                console.log('StopList not found');
            }
            $httpService.deleteStoplist(stoplist.id, function () {
                $log.info('Stoplist deleted successfully');
                $scope.refresh();
            })
        };

        $scope.getOwnerId = function (stopList) {
            if (!!stopList && !!stopList.owner && stopList.owner.length > 0) {
                return stopList.owner[0].id;
            }
        };

        $scope.refresh = function () {
            $httpService.findStopLists(function (result) {
                $scope.stopLists = result.data;
            });
        };

        //Инициализация
        $scope.refresh();

        $scope.login = function () {
            window.location.replace('#/login');
        }

        $scope.createDate = function (dateVal,isShowTime) {
            if (dateVal == null) return "Не запланирован";
            var date = new Date(dateVal);
            var result = (date.getDate().toString().length == 1 ? '0' + date.getDate() : date.getDate()) + "."
                + ((date.getMonth() + 1).toString().length == 1 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + "."
                + date.getFullYear();

            if(isShowTime) result += " " + date.getHours() + ":" + (date.getMinutes()>9 ? date.getMinutes() : "0"+date.getMinutes());
            return result;
        };

        $scope.today = new Date();
        $scope.yesterday = new Date(Math.round(new Date().getTime()) - 86400000);

        console.log("today="+$scope.today.getTime());
        console.log("yesterday="+$scope.yesterday.getTime());

        $scope.todaycount = 15;
        $scope.yesterdaycount = 43;

        $http.get("/compare/api/requestcount",
            {params:{
                    startDate: $scope.today.getTime()-86400000,
                    endDate: $scope.today.getTime()
                }}).success(function (data) {
                    $scope.todaycount=data;
            });

        $http.get("/compare/api/requestcount",
            {params:{
                    startDate: $scope.yesterday.getTime()-86400000,
                    endDate: $scope.yesterday.getTime()
                }}).success(function (data) {
                    $scope.yesterdaycount=data;
            });
    });