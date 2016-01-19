angular.module('orderDetail', []).controller('orderDetailCtrl', function($scope, $http) {
    try {
        $scope.orderBean = eval("(" + window.jsparams.getJson() + ")");
        console.log(window.jsparams.getJson());
    } catch (e) {
        console.log(e);
        $scope.orderBean = {
            id: 49,
            number: "16011809494510297134",
            line: {
                company: {
                    id: 2
                }
            }
        };
    } finally {

    }
    //获取物流信息
    $http.jsonp(
        HOST + "orders/detail/?callback=JSON_CALLBACK&orderId=" + $scope.orderBean.id + "&companyId=" + $scope.orderBean.line.company.id
    ).success(function(data, status, headers, config) {
        console.log("success == " + JSON.stringify(data));
        if (data.datas == undefined) {
            alert(data.msg);
        } else {
            $scope.orderBean = data.datas;
            if ($scope.orderBean.status == 0) {
                $scope.orderBean.statusStr = "待受理";
                $("#bt0").html("联系电话");
                $("#bt1").html("取消订单");
                $("#bt2").html("提醒受理");
                callPhone($("#bt0"), $scope.orderBean.line.company.phone);
                toastAlways($("#bt2"), "提醒成功！");
                $('#bt1').click(function() {
                    $("#content").html("确定取消吗？");
                    $('#myModal').modal();
                });
                httpRequest($http, $("#ok"), null, HOST + "orders/cancel/" + $scope.orderBean.id + "/" + $scope.orderBean.status, null, null, function() {
                    $('#myModal').modal("hide");
                }, function() {
                    toastAlways("操作失败，请重试！");
                });
            } else if ($scope.orderBean.status == 1) {
                $scope.orderBean.statusStr = "待发货";
                $("#bt0").html("联系电话");
                $("#bt1").html("申请取消");
                $("#bt2").html("提醒发货");
                callPhone($("#bt0"), $scope.orderBean.line.company.phone);
                toastAlways($("#bt2"), "提醒成功！");
                $('#bt1').click(function() {
                    $("#content").html("确定取消吗？");
                    $('#myModal').modal();
                });
                httpRequest($http, $("#ok"), null, HOST + "orders/cancel/" + $scope.orderBean.id + "/" + $scope.orderBean.status, null, null, function() {
                    $('#myModal').modal("hide");
                }, function() {
                    toastAlways("操作失败，请重试！");
                });
            } else if ($scope.orderBean.status == 2 || $scope.orderBean.status == 3) {
                if ($scope.orderBean.status == 2) {
                    $scope.orderBean.statusStr = "待收货";
                } else {
                    $scope.orderBean.statusStr = "待确认";
                }
                $("#bt0").html("联系电话");
                $("#bt1").html("申请理赔");
                $("#bt2").html("确认收货");
                $('#bt1').click(function() {
                    window.jscomm.goToNewAct( "com.woyaofa.ui.WebActivity", '[{"key":"title","value":"申请理赔"},{"key":"url","value":"file:///android_asset/www/html/claim.html"},{"key":"jsonBean","value":\'' + JSON.stringify($scope.orderBean) + '\'}]');
                });
                $('#bt2').click(function() {
                    $("#content").html("确定收货吗？");
                    $('#myModal').modal();
                });
                callPhone($("#bt0"), $scope.orderBean.line.company.phone);
                httpRequest($http, $("#ok"), null, HOST + "orders/comfirm/" + $scope.orderBean.id, null, null, function() {
                    $('#myModal').modal("hide");
                }, function() {
                    toastAlways("操作失败，请重试！");
                });
            } else if ($scope.orderBean.status == 4) {
                $scope.orderBean.statusStr = "待评价";
                $("#bt0").html("联系电话");
                $("#bt1").html("删除");
                $("#bt2").html("评价");
                callPhone($("#bt0"), $scope.orderBean.line.company.phone);
                goToNewAct($("#bt2"), "com.woyaofa.ui.order.CommentActivity", '[{"key":"jsonBean","value":\'' + JSON.stringify($scope.orderBean) + '\'}]');
                $('#bt1').click(function() {
                    $("#content").html("确定删除吗？");
                    $('#myModal').modal();
                });
                httpRequest($http, $("#ok"), null, HOST + "orders/delete/" + $scope.orderBean.id, null, null, function() {
                    $('#myModal').modal("hide");
                }, function() {
                    toastAlways("操作失败，请重试！");
                });
            } else if ($scope.orderBean.status == -1) {
                $scope.orderBean.statusStr = "已完成";
                $("#bt0").html("永久删除");
                $("#bt2").html("联系电话");
                $("#bt2").css({
                    "width": "67%"
                });
                $("#bt1").css({
                    "display": "none"
                });
                callPhone($("#bt2"), $scope.orderBean.line.company.phone);
                $('#bt0').click(function() {
                    $("#content").html("确定删除吗？");
                    $('#myModal').modal();
                });
                httpRequest($http, $("#ok"), null, HOST + "orders/delete/" + $scope.orderBean.id, null, null, function() {
                    $('#myModal').modal("hide");
                }, function() {
                    toastAlways("操作失败，请重试！");
                });
            }
        }
    }).error(function(data, status, headers, config) {
        alert("请求失败！" + data);
    });
    $("#company").click(function() {
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"url","value":"file:///android_asset/www/html/company_index.html"},{"key":"jsonBean","value":\'' + JSON.stringify($scope.orderBean.line.company) + '\'}]');
    });
});
