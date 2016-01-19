var module = angular.module('companyDetail', []);
module.controller('companyDetailCtrl', function($scope, $http, companyDetailService) {
    try {
        console.log(window.jsparams.getJson());
        $scope.companyBean = eval("(" + window.jsparams.getJson() + ")");
    } catch (e) {
        console.log(e);
        $scope.companyBean = {
            id: 2
        }
    } finally {

    }
    $http.jsonp(
        HOST + 'companies/detail/?companyId=' + $scope.companyBean.id + "&callback=JSON_CALLBACK"
    ).success(function(data, status, headers, config) {
        if (data.datas == undefined) {
            alert(data.msg);
        } else {
            $scope.companyBean = data.datas;
            console.log(JSON.stringify(data));
        }
    }).error(function(data, status, headers, config) {
        alert("请求失败！" + data);
    });

    $scope.clickImg = function($event, src) {
        // console.log(src);
        // console.log(this);
        console.log($event.target);
        $("#overviewImg").attr("src", src);
        if ($("#overview").css("display") == "none") {
            $("#overview").css("display", "block");
        } else {
            $("#overview").css("display", "none");
        }
    };

});
module.factory('companyDetailService', function() {
    return {

    };
});
