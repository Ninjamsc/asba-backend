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
}).factory('$c', function () {
    this.arrayToMatrix = function (arr, n) {
        var res = [];
        for (var i = 0; i < (arr.length / n); i++) {
            res.push(arr.slice(i * n, (i + 1) * n));
        }
        return res;
    };
    return this;
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
        function http(method, clbck) {
            http(method, clbck, {}, 'GET', {})
        }

        function http(url, clbck, method, params, data) {
            var defaultParams = {};
            if (!!params) {
                defaultParams = angular.extend(defaultParams, params);
            }
            return $http({
                method: method,
                url: url,
                params: defaultParams,
                data: data
            }).then(function (response) {
                if (!!clbck) {
                    clbck(response)
                }
            }, logErrorCallback);
        };


        var singleStoplist = function (id, callback, method, params, data) {
            console.log();
            var url = 'rpe/api/rest/stoplist/';
            if (!!id) {
                url = url + id;
            }
            if (id === 'test') {
                url = 'src/json/stop-list-edit.json';
            }
            http(url, callback, method, params, data)
        };

        this.findStopLists = function (callback) {
            singleStoplist(null, callback, 'GET')
        };


        this.getStoplist = function (id, callback) {
            console.log('getStoplist');
            singleStoplist(id, callback, 'GET')
        };

        this.addStoplist = function (stoplist, callback) {
            singleStoplist(null, callback, 'POST', {}, stoplist)
        };

        this.editStoplist = function (stoplist, callback) {
            singleStoplist(null, callback, 'PUT', {}, stoplist)
        };

        this.deleteStoplist = function (id, callback) {
            singleStoplist(id, callback, 'DELETE')
        };

        this.deleteStoplist2 = function (id, callback) {
            console.log();
            var url = 'compare/api/stoplist/';
            if (!!id) {
                url = url + id;
            }
            http(url, callback, 'DELETE')
        };

        this.findCompareResult = function (id, callback) {
            var url = 'rpe/api/rest/compare-result/' + id;
            if (id == 'compare-result-view.json') {
                url = 'src/json/compare-result-view.json';
            }
            http(url, callback, "GET", {}, {})
        };

        this.deletePhoto = function (listId, itemId, callback) {
            var url = 'compare/api/stoplist/'+listId + '/entry/' + itemId;

            http(url, callback, "DELETE", {}, {})
        };


        return this;
    })/*.service('$modalService', ['$modal',
 // NB: For Angular-bootstrap 0.14.0 or later, use $uibModal above instead of $modal
 function ($modal) {

 var modalDefaults = {
 backdrop: true,
 keyboard: true,
 modalFade: true,
 templateUrl: '/src/modal.html'
 };

 var modalOptions = {
 closeButtonText: 'Close',
 actionButtonText: 'OK',
 headerText: 'Proceed?',
 bodyText: 'Perform this action?'
 };

 this.showModal = function (customModalDefaults, customModalOptions) {
 if (!customModalDefaults) customModalDefaults = {};
 customModalDefaults.backdrop = 'static';
 return this.show(customModalDefaults, customModalOptions);
 };

 this.show = function (customModalDefaults, customModalOptions) {
 //Create temp objects to work with since we're in a singleton service
 var tempModalDefaults = {};
 var tempModalOptions = {};

 //Map angular-ui modal custom defaults to modal defaults defined in service
 angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);

 //Map modal.html $scope custom properties to defaults defined in service
 angular.extend(tempModalOptions, modalOptions, customModalOptions);

 if (!tempModalDefaults.controller) {
 tempModalDefaults.controller = function ($scope, $modalInstance) {
 $scope.modalOptions = tempModalOptions;
 $scope.modalOptions.ok = function (result) {
 $modalInstance.close(result);
 };
 $scope.modalOptions.close = function (result) {
 $modalInstance.dismiss('cancel');
 };
 };
 }

 return $modal.open(tempModalDefaults).result;
 };

 }])*/;
