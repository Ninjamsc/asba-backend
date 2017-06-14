angular.module('login', ['ui.router', 'commons'])
    .controller('loginController', function ($scope, $log, $state, $stateParams,
                                             $httpService, $c,
                                             RuleErrorType) {
        console.log("login");

    });