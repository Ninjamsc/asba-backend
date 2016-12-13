angular.module('compare-result-view', ['ui.router','commons'])
    .controller('compareResultViewController', function ($scope, $log, $state, $stateParams) {

        $scope.stateListId = $stateParams.id; //todo stateParams

        console.log($stateParams);

        /*$httpService.findStopLists(function (result) {
         $scope.stopLists = result.data;
         });*/
    });