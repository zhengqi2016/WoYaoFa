angular.module('feedback', []).controller('feedbackCtrl', function($scope, $http) {
    // var account = eval("(" + window.jsparams.getJson() + ")");
    var account = eval("(" + window.jscomm.getAccountWithLogin() + ")");
    // var account = {
    //     id: 1
    // };
    angular.element("#submit").click(function() {
        if ($scope.content) {
            var datas = [{
                'key': 'content',
                'value': $scope.content
            }, {
                'key': 'accountId',
                'value': account.id
            }];
            window.jsrequest.requestPost(HOST + 'feedbacks/add', JSON.stringify(datas));
            // $http({
            //     method: 'post',
            //     dataType: 'json',
            //     url: HOST + 'feedbacks/add',
            //     data: $.param({
            //         'accountId': account.id,
            //         'content': $scope.content
            //     }),
            //     headers: {
            //         'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            //     }
            // }).success(function(data, status, headers, config) {
            //     console.log("success");
            //     window.jscomm.toastAlways(data.msg);
            //     if (data.datas == undefined) {} else {
            //         window.jscomm.onBack();
            //     }
            // }).error(function(data, status, headers, config) {
            //     console.log("err " + status + " | " + headers + " | " + config);
            //     window.jscomm.toastAlways("请求失败，请重试！");
            // });
        } else {
            alert("内容不能为空哦！");
        }
    });
});
