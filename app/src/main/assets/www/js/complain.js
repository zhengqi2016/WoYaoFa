angular.module('complain', []).controller('complainCtrl', function($scope, $http) {
    try {
        console.log(window.jsparams.getJson());
        $scope.actionBean = eval("(" + window.jsparams.getJson() + ")");
    } catch (e) {
        console.log(e);
    } finally {

    }
    $("#submit").click(function() {
        try {
            $scope.accountBean = eval("(" + window.jscomm.getAccountWithLogin() + ")");
            if ($scope.reason == undefined) {
                alert("请填写投诉理由！");
                return;
            }
            if ($scope.explain == undefined) {
                $scope.explain = "";
            }
            var datas = [{
                'key': 'content',
                'value': $scope.reason + " " + $scope.explain
            }, {
                'key': 'accountId',
                'value': $scope.accountBean.id
            }, {
                'key': 'type',
                'value': 0
            }, {
                'key': 'companyId',
                'value': 0
            }, {
                'key': 'lineId',
                'value': 0
            }];
            if ($scope.actionBean.action == "company") {
                datas[2].value = 0;
                datas[3].value = $scope.actionBean.id;
            } else {
                datas[2].value = 1;
                datas[4].value = $scope.actionBean.id;
            }
            window.jsrequest.requestPost(HOST + 'complaints/add', JSON.stringify(datas));
            // $http({
            //     method: 'post',
            //     dataType: 'json',
            //     url: HOST + 'complaints/add',
            //     data: $.param(datas),
            //     headers: {
            //         'Content-Type': 'application/x-www-form-urlencoded'
            //     }
            // }).success(function(data, status, headers, config) {
            //     console.log(JSON.stringify(data));
            //     alert(data.msg);
            // }).error(function(data, status, headers, config) {
            //     alert("请求失败，请重新尝试！");
            // });
        } catch (e) {
            console.log(e);
        } finally {

        }
    });
});
