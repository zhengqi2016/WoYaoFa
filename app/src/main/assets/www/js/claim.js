angular.module('claim', []).controller('claimController', function($scope, $http) {
    try {
        $scope.orderBean = eval("(" + window.jsparams.getJson() + ")");
        console.log(window.jsparams.getJson());
    } catch (e) {
        console.log(e);
    } finally {

    }
    angular.element("#submit").click(function() {
        $http.jsonp({
            url: HOST + ''
        }).success(function(data, status, headers, config) {
            window.jscomm.toastAlways(data.datas.resultList[0].name + "  " + status);
        }).error(function(data, status, headers, config) {
            window.jscomm.toastAlways(data);
        });
    });
    angular.element("#phone").click(function() {
        window.jscomm.callPhone($scope.orderBean.line.company.phone);
    });
});
