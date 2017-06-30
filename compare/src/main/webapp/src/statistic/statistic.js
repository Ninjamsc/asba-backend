angular.module('statistic', ['ui.router', 'commons'])
    .controller('statisticController', function ($scope, $log, $state, $stateParams, $http) {
        $scope.countArray = [];

        $scope.createDate = function (dateVal,isShowTime) {
            if (dateVal == null) return "Не запланирован";
            var date = new Date(dateVal);
            var result = (date.getDate().toString().length == 1 ? '0' + date.getDate() : date.getDate()) + "."
                + ((date.getMonth() + 1).toString().length == 1 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + "."
                + date.getFullYear();

            if(isShowTime) result += " " + date.getHours() + ":" + (date.getMinutes()>9 ? date.getMinutes() : "0"+date.getMinutes());
            return result;
        };

        $scope.setDate = function (dateType) {

            console.log("setDate startDate"+$scope.startDate);
            console.log("endDate endDate"+$scope.endDate);

            $http.get("/compare/api/requestcount/range/list",
                {params:{
                    startDate: $scope.startDate.getTime(),
                    endDate: $scope.endDate.getTime()
                }}).success(function (data) {
                    $scope.countArray = data;
            });

        };

        $scope.startDate = new Date(Math.round(new Date().getTime()) - 86400000);
        $scope.endDate = new Date();
        $scope.setDate();

        $scope.today = new Date();
        $scope.yesterday = new Date(Math.round(new Date().getTime()) - 86400000);

        $http.get("/compare/api/requestcount",
            {params:{
                startDate: $scope.yesterday.getTime(),
                endDate: $scope.today.getTime()//+86400000
            }}).success(function (data) {
            $scope.total=data;
        });

        $scope.loadGoodIds = function (c) {
            if (c.isbiggerView == undefined || c.isbiggerView == null || !c.isbiggerView) {
                c.isbiggerView = true;
                $http.get("/compare/api/requestids/approved",
                    {
                        params: {
                            startDate: c.startDate,
                            endDate: c.endDate//+86400000
                        }
                    }).success(function (data) {
                    c.biggerCountIds = data;
                });
            } else c.isbiggerView = false;
        }
        $scope.loadLowIds = function (c) {
            if (c.isloverView == undefined || c.isloverView == null || !c.isloverView) {
                c.isloverView = true;
                $http.get("/compare/api/requestids/disapproved",
                    {
                        params: {
                            startDate: c.startDate,
                            endDate: c.endDate//+86400000
                        }
                    }).success(function (data) {
                    c.lowerCountIds = data;
                });
            } else c.isloverView = false;
        }

    });