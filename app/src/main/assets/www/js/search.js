angular.module('search', []).controller('searchCtrl', function($scope, $http) {
    angular.element("#submit").click(function() {
        if ($scope.keyword) {
            $http.jsonp(
                HOST + 'companies/search?callback=JSON_CALLBACK&keyword=' + $scope.keyword
            ).success(function(data, status, headers, config) {
                console.log(JSON.stringify(data));
                if (data.datas) {
                    $scope.companies = data.datas;
                    if ($scope.companies.length == 0) {
                        alert("没有你想要的内容！");
                    }
                } else {
                    alert("" + data.msg);
                }
            }).error(function(data, status, headers, config) {
                window.jscomm.toastAlways("请求失败了，请重试！");
            });
        } else {
            alert("关键词不能为空哦！");
        }
    });
    $scope.goto = function(company) {
        console.log(JSON.stringify(company));
        var parms = {
            id: company.id
        }
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"url","value":"file:///android_asset/www/html/company_index.html"},{"key":"jsonBean","value":\'' + JSON.stringify(parms) + '\'}]');
    };
});
