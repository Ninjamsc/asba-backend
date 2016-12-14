angular.module('commons', []).constant('contextualClass', {
    ACTIVE: {code: 'active'},
    SUCCESS: {code: 'success'},
    WARNING: {code: 'warning'},
    DANGER: {code: 'danger'},
    INFO: {code: 'info'}


}).factory('$httpService',
    function ($log, $http, $httpParamSerializer) {
        var logErrorCallback = function (error) {
            $log.error(error);
        };
        this.buildUrl = function (url, params) {
            var serializedParams = $httpParamSerializer(params);

            if (serializedParams.length > 0) {
                $log.info(serializedParams);
                url += ((url.indexOf('?') === -1) ? '?' : '&') + serializedParams;
            }

            return url;
        };

        //todo defer return result
        function http(method, clbck, params) {
            var defaultParams = {};
            if (!!params) {
                defaultParams = angular.extend(defaultParams, params);
            }
            var urlTemplate = './';
            return $http({
                method: 'GET',
                url: urlTemplate + method,
                params: defaultParams
            }).then(function (response) {
                if (!!clbck) {
                    clbck(response)
                }
            }, logErrorCallback);
        };

        this.findStopLists = function (callback) {
            var method = 'src/json/stop-list-registry.json';
            http(method, callback)
        };

        this.findCompareResult = function (id, callback) {
            var method = 'rpe/api/rest/compare-result/' + id;
            // var method = 'src/json/compare-result-view.json';
            http(method, callback);
        };

        return this;
    });
