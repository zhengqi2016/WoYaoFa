angular.module('logisticsInfo', []).controller('logisticsInfoCtrl', function($scope, $http) {
    try {
        $scope.number = window.jsparams.getJson();
        console.log(JSON.stringify($scope.number));
    } catch (e) {
        console.log(e);
        // $scope.number = "15122912151838581796";
    } finally {

    }
    //获取物流信息
    $http.jsonp(
        HOST + "orders/getOrder/?callback=JSON_CALLBACK&number=" + $scope.number
    ).success(function(data, status, headers, config) {
        console.log(JSON.stringify(data));
        if (data.datas == undefined) {
            alert(data.msg);
        } else {
            $scope.orderBean = data.datas;
            if ($scope.orderBean.status == 0) {
                $scope.orderBean.statusStr = "待受理";
            } else if ($scope.orderBean.status == 1) {
                $scope.orderBean.statusStr = "待发货";
            } else if ($scope.orderBean.status == 2) {
                $scope.orderBean.statusStr = "待收货";
            } else if ($scope.orderBean.status == 3) {
                $scope.orderBean.statusStr = "待确认";
            } else if ($scope.orderBean.status == 4) {
                $scope.orderBean.statusStr = "待评价";
            } else if ($scope.orderBean.status == -1) {
                $scope.orderBean.statusStr = "已完成";
            }
        }
    }).error(function(data, status, headers, config) {
        alert("请求失败！" + data);
    });
    $("#company").click(function() {
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"url","value":"file:///android_asset/www/html/company_index.html"},{"key":"jsonBean","value":\'' + JSON.stringify($scope.orderBean.line.company) + '\'}]');
    });
});
