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
var module = angular.module('test', [

]);
module.controller('testCtrl', function($scope, $http, testService) {
    $scope.testword = "dads";
    $scope.name = "abc";
    $scope.pwd = "qwer";
    
    testService.getData(1);

    $('#bt').click(function() {
        alertConfirm({
            title: "Are you sure?",
            text: "You will not be able to",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Yes, delete it!",
            cancelButtonText: "No",
            closeOnConfirm: false,
            // closeOnCancel: false
        }, function(isConfirm) {
            if (isConfirm) {
                alertToast("Toast", "Your imaginary file has been deleted.");
            } else {

            }
        })
    });
});
module.factory('testService', function($rootScope, $http) {
    return {
        getData: function(id) {

            // console.log($rootScope.testword + HOST + 'companies/home/?companyId=' + id + "&callback=JSON_CALLBACK");
            $http({
                method: 'get',
                dataType: 'json',
                url: 'http://123.57.147.82:8001/main',
                params: {
                    'id': '2'
                },
                data: {
                    'accountId': '1'
                },
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).success(function(data, status, headers, config) {
                console.log(JSON.stringify(data));
            }).error(function(data, status, headers, config) {
                console.log("请求失败！" + status + " == " + data);
            });
        }
    }
});
