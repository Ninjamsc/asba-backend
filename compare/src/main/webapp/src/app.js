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

    //todo путь считается относительно index.html, некрасиво. Проще всего перенести его в src/* и подправить nginx.conf
    //но тогда к node-modules досуп через ../ будет

    $stateProvider.state('stop-lists-registry', {
        url: '/stoplists?auth',
        params: {
            id: null
        },
        templateUrl: 'src/stop-lists/stop-lists-registry.html',
        controller: 'stopListsRegistryController'

    }).state('stop-lists-edit', {
        url: '/stoplists/edit?stoplistId',
        templateUrl: 'src/stop-lists/stop-lists-edit.html',
        controller: 'stopListsEditController'

    }).state('stop-lists-add', {
        url: '/stoplists/add',
        templateUrl: 'src/stop-lists/stop-lists-edit.html',
        controller: 'stopListsEditController'

    }).state('compare-result', {
        url: '/compare?requestId',
        templateUrl: 'src/compare/compare-result.html',
        controller: 'compareResultViewController'

    }).state('login', {
        url: '/login',
        templateUrl: 'src/login/login.html',
        controller: 'loginController'
    });

});

