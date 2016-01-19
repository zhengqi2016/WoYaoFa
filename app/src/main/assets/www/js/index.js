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
angular.module('index', []).controller('IndexController', function($scope, $http) {
    //请求首页轮播图
    $http.jsonp(HOST + 'home/ads?callback=JSON_CALLBACK')
        .success(function(data, status, headers, config) {
            console.log("ads== " + JSON.stringify(data));
            if (data.datas == undefined) {
                // alert(data.msg);
            } else {
                $scope.ads = data.datas;
            }
        }).error(function(data, status, headers, config) {

        });
    // $http({
    //     method: 'get',
    //     dataType: 'jsonp',
    //     url: HOST + 'home/ads?callback=JSON_CALLBACK',
    //     jsonp:'JSON_CALLBACK'
    // }).success(function(data, status, headers, config) {
    //     console.log(JSON.stringify(data));
    //
    // }).error(function(data, status, headers, config) {
    //     alert("请求失败！" + data);
    // });

    angular.element("#item_find_express").click(function() {
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"title","value":"发快递"},{"key":"url","value":"http://m.kuaidi100.com/all/"}]');
    });
    angular.element("#item_find_logistics").click(function() {
        window.jscomm.goToNewAct("com.woyaofa.ui.order.FindLogisticsActivity");
    });
    angular.element("#search_logistics").click(function() {
        // $scope.action = "logistics";
        // angular.element("#search_logistics")[0].src = "img/logistics_icon_s.png";
        // angular.element("#search_express")[0].src = "img/courier_icon.png";
    });
    angular.element("#search_express").click(function() {
        // $scope.action = "search_express";
        // angular.element("#search_logistics")[0].src = "img/logistics_icon.png";
        // angular.element("#search_express")[0].src = "img/courier_icon_s.png";
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"title","value":"发快递"},{"key":"url","value":"http://m.kuaidi100.com/"}]');
    });
    angular.element("#submit").click(function() {
        if (!$scope.keyword) {
            window.jscomm.toastAlways("请输入运单号哦！");
        } else {
            window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"title","value":"查看物流"},{"key":"url","value":"file:///android_asset/www/html/logistics_info.html"},{"key":"jsonBean","value":"' + $scope.keyword + '"}]');
        }
    });
    $('.ads').click(function() {
        console.log($(this).attr("alt"));
        window.jscomm.goToNewAct("com.woyaofa.ui.WebActivity", '[{"key":"url","value":"' + $(this).attr("alt") + '"}]');
    });
});
