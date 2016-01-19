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

angular.module('line_detail', []).controller('lineDetailCtrl', function($scope, $http) {
    try {
        $scope.lineBean = eval("(" + window.jsparams.getJson() + ")");
        console.log(window.jsparams.getJson());
        $scope.loadOthers = function() {
            $http.jsonp(
                HOST + 'lines/others/?callback=JSON_CALLBACK&companyId=' + $scope.lineBean.company.id + "&lineId=" + $scope.lineBean.id
            ).success(function(data, status, headers, config) {
                console.log(JSON.stringify(data));
                if (data.datas == undefined) {
                    // alert(data.msg);
                } else {
                    $scope.otherLines = data.datas;
                    if ($scope.otherLines) {
                        // $scope.lineBean.commentNum = 0;
                        // $scope.lineBean.commentTotalScore = 0;
                        for (var i = 0; i < $scope.otherLines.length; i++) {
                            var line = $scope.otherLines[i];
                            $scope.lineBean.commentNum += line.commentNum;
                            $scope.lineBean.commentTotalScore += line.commentTotalScore;
                        }
                        $scope.lineBean.commentTotalScore = $scope.lineBean.commentTotalScore / ($scope.otherLines.length + 1);
                    }
                }
            }).error(function(data, status, headers, config) {
                alert("请求失败，请重新尝试！");
            });
        };
        $http.jsonp(
            HOST + 'lines/detail/?callback=JSON_CALLBACK&lineId=' + $scope.lineBean.id
        ).success(function(data, status, headers, config) {
            console.log("line_detail == " + JSON.stringify(data));
            if (data.datas == undefined) {
                alert(data.msg);
            } else {
                $scope.lineBean = data.datas;
                $scope.loadOthers();
            }
        }).error(function(data, status, headers, config) {
            alert("请求失败，请重新尝试！");
        });

    } catch (e) {
        console.log(e);
    }
    angular.element("#mRight").click(function() {
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"title","value":"投诉"},{"key":"url","value":"file:///android_asset/www/html/complain.html"},{"key":"jsonBean","value":\'{"action":"line","id":"' + $scope.lineBean.id + '"}\'}]');
    });
    angular.element("#evaluation").click(function() {
        window.jscomm.goToNewAct("com.woyaofa.ui.order.CommentListActivity", '[{"key":"bean","value":\'' + JSON.stringify($scope.lineBean) + '\'}]');
    });
    angular.element("#company").click(function() {
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"title","value":"公司详情"},{"key":"url","value":"file:///android_asset/www/html/company_detail.html"},{"key":"jsonBean","value":\'' + JSON.stringify($scope.lineBean.company) + '\'}]');
    });
    angular.element("#order").click(function() {
        window.jscomm.goToNewAct("com.woyaofa.ui.order.PlaceAnOrderActivity", '[{"key":"bean","value":\'' + window.jsparams.getJson() + '\'}]');
    });
    angular.element("#phone").click(function() {
        window.jscomm.callPhone($scope.lineBean.company.phone);
    });
    angular.element("#option_phone").click(function() {
        window.jscomm.callPhone($scope.lineBean.company.phone);
    });
    angular.element("#collect").click(function() {
        try {
            $scope.accountBean = eval("(" + window.jscomm.getAccountWithLogin() + ")");
            $http.jsonp(
                HOST + 'collections/add/?callback=JSON_CALLBACK&accountId=' + $scope.accountBean.id + "&lineId=" + $scope.lineBean.id + "&type=1"
            ).success(function(data, status, headers, config) {
                console.log(JSON.stringify(data));
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
    $("#address").click(function() {
        var addr = {};
        addr.province = $scope.lineBean.company.province;
        addr.city = $scope.lineBean.company.city;
        addr.district = $scope.lineBean.company.district;
        addr.address = $scope.lineBean.company.address;
        addr.title = $scope.lineBean.company.name;
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"title","value":"地图"},{"key":"url","value":"file:///android_asset/www/html/map.html"},{"key":"jsonBean","value":\'' + JSON.stringify(addr) + '\'}]');
    });
});
