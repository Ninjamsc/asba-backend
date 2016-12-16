angular.module('commons', []).constant('contextualClass', {
    ACTIVE: {code: 'active'},
    SUCCESS: {code: 'success'},
    WARNING: {code: 'warning'},
    DANGER: {code: 'danger'},
    INFO: {code: 'info'}


}).constant('RuleErrorType', {

    RULE_4_2_1: {
        ID: "4.2.1",
        NAME: "Фотография, прикрепленная к заявке, существенно отличается от других фотографий заемщика, имеющихся в базе"
    },
    RULE_4_2_2: {
        ID: "4.2.2",
        NAME: "Фотография, прикрепленная к заявке, идентична имеющейся в базе"
    },
    RULE_4_2_3: {
        ID: "4.2.3",
        NAME: "Возможно соответствие с клиентом из банковского СТОП-ЛИСТА"
    },
    RULE_4_2_4: {
        ID: "4.2.4",
        NAME: "Возможно соответствие с клиентом из общего СТОП-ЛИСТА"
    },
    RULE_4_2_5: {
        ID: "4.2.5",
        NAME: "Возможно несоответствие фотографии в паспорте и фотографии, прикрепленной к заявке"
    }
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
            if (id == 'compare-result-view.json') {
                method = 'src/json/compare-result-view.json';
            }
            http(method, callback);
        };

        return this;
    });
