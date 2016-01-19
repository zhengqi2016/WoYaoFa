window.onload = function() {
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        // paginationClickable: true,
        // nextButton: '.swiper-button-next',
        // prevButton: '.swiper-button-prev',
        autoplay: 3000,
        // slidesPerView: 'auto',
        autoplayDisableOnInteraction: false,
        direction: 'horizontal',
        loop: true
    });
}
angular.module('companyIndex', []).controller('companyIndexCtrl', function($scope, $http) {
    try {
        $scope.companyBean = eval("(" + window.jsparams.getJson() + ")");
        console.log("come == " + JSON.stringify($scope.companyBean));
        $scope.companyBean.commentNum = 0;
        $scope.companyBean.commentTotalScore = 0;
    } catch (e) {
        console.log(e);
        // $scope.companyBean = {
        //     id: 32
        // }
    } finally {

    }
    $http.jsonp(
        HOST + 'companies/home/?callback=JSON_CALLBACK&companyId=' + $scope.companyBean.id
    ).success(function(data, status, headers, config) {
        console.log("success == " + JSON.stringify(data));
        if (data.datas == undefined) {
            alertToast(null, data.msg);
        } else {
            $scope.companyBean = data.datas;
            if ($scope.companyBean.lines) {
                $scope.companyBean.commentNum = 0;
                $scope.companyBean.commentTotalScore = 0;
                for (var i = 0; i < $scope.companyBean.lines.length; i++) {
                    var line = $scope.companyBean.lines[i];
                    $scope.companyBean.commentNum += line.commentNum;
                    $scope.companyBean.commentTotalScore += line.commentTotalScore;
                }
                $scope.companyBean.commentTotalScore = $scope.companyBean.commentTotalScore / $scope.companyBean.lines.length;
            }
        }
    }).error(function(data, status, headers, config) {
        alertToast(null, "请求失败！" + data);
    });
    angular.element("#mBack").click(function() {
        window.jscomm.onBack();
    });
    angular.element("#mRight").click(function() {
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"title","value":"投诉"},{"key":"url","value":"file:///android_asset/www/html/complain.html"},{"key":"jsonBean","value":\'{"action":"company","id":"' + $scope.companyBean.id + '"}\'}]');
    });
    angular.element("#company_content").click(function() {
        var datas = {
            id: $scope.companyBean.id
        }
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"title","value":"公司详情"},{"key":"url","value":"file:///android_asset/www/html/company_detail.html"},{"key":"jsonBean","value":\'' + JSON.stringify(datas) + '\'}]');
    });
    // angular.element("#evaluation").click(function() {
    //     window.jscomm.goToNewAct("com.woyaofa.ui.order.CommentListActivity", "[{\"key\":\"bean\",\"value\":\"'" + window.jsparams.getJson() + "'\"}]");
    // });
    angular.element("#phone").click(function() {
        window.jscomm.callPhone($scope.companyBean.phone);
    });
    angular.element("#option_phone").click(function() {
        window.jscomm.callPhone($scope.companyBean.phone);
    });
    $("#address").click(function() {
        var addr = {};
        addr.province = $scope.companyBean.province;
        addr.city = $scope.companyBean.city;
        addr.district = $scope.companyBean.district;
        addr.address = $scope.companyBean.address;
        addr.title = $scope.companyBean.name;
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"title","value":"地图"},{"key":"url","value":"file:///android_asset/www/html/map.html"},{"key":"jsonBean","value":\'' + JSON.stringify(addr) + '\'}]');
    });

    angular.element("#collect").click(function() {
        try {
            $scope.accountBean = eval("(" + window.jscomm.getAccountWithLogin() + ")");
            $http.jsonp(
                HOST + 'collections/add?&callback=JSON_CALLBACK&type=0&companyId=' + $scope.companyBean.id + '&accountId=' + $scope.accountBean.id
            ).success(function(data, status, headers, config) {
                console.log(JSON.stringify(data));
                if (data.code == "2000") {
                    // $("#collect").attr();
                    // alertToast(null, data.msg);
                }
                alertToast(null, data.msg);
            }).error(function(data, status, headers, config) {
                alertToast(null, "请求失败，请重新尝试！");
            });
        } catch (e) {
            console.log(e);
        } finally {

        }
    });
    $scope.goto = function(line) {
        console.log(JSON.stringify(line));
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"url","value":"file:///android_asset/www/html/line_detail.html"},{"key":"jsonBean","value":\'' + JSON.stringify(line) + '\'}]');
    };
});
