var myApp = angular.module('myApp', [
    'ui.router',
    'ngRoute',
    'ngCookies',
    'stop-lists-registry',
    'stop-lists-edit',
    'compare-result-view',
    'login',
    'commons']);

myApp.config(function ($stateProvider, $routeProvider, $urlRouterProvider) {

    $stateProvider.state('stop-lists-registry', {
        url: '/stoplists?auth',
        params: {
            id: null
        },
        templateUrl: 'app/stop-lists/stop-lists-registry.html',
        controller: 'stopListsRegistryController'

    }).state('stop-lists-edit', {
        url: '/stoplists/edit?stoplistId',
        templateUrl: 'app/stop-lists/stop-lists-edit.html',
        controller: 'stopListsEditController'

    }).state('stop-lists-add', {
        url: '/stoplists/add',
        templateUrl: 'app/stop-lists/stop-lists-edit.html',
        controller: 'stopListsEditController'

    }).state('compare-result', {
        url: '/compare?requestId',
        templateUrl: 'app/compare/compare-result.html',
        controller: 'compareResultViewController'

    }).state('login', {
        url: '/login',
        templateUrl: 'app/login/login.html',
        controller: 'loginController'
    });

});

