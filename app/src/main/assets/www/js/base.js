document.getElementsByTagName("html")[0].style.setProperty("font-size", (document.body.clientWidth / 360 * 12) + "px");
// var HOST = 'http://192.168.199.197:8080/Wuliu-Api/webapi/';
// var HOST = 'http://192.168.199.100:8080/Wuliu-Api/webapi/';
var HOST = 'http://123.57.147.82:8080/Wuliu-Api/webapi/';
var mBack = document.getElementById("mBack");
if (mBack) {
    mBack.onclick = function() {
        window.jscomm.onBack();
    };
}

function callPhone(element, phoneNum) {
    element.click(function() {
        window.jscomm.callPhone(phoneNum);
    });
}

function toastAlways(element, str) {
    element.click(function() {
        window.jscomm.toastAlways(str);
    });
}
//跳转界面函数，packagePath为java的activity的路径名，jsonBean为json的跳转传递的参数
function goToNewAct(element, packagePath, jsonBean) {
    element.click(function() {
        window.jscomm.goToNewAct(packagePath, jsonBean);
    });
}

function goToNewAct(element, packagePath) {
    element.click(function() {
        window.jscomm.goToNewAct(packagePath);
    });
}
//params为get的json参数，最后以？key=value的形式加到urlPath后面，datas为post的参数
function httpRequest($http, element, requestMethod, urlPath, params, datas, sucFun, errFun) {
    if (sucFun == undefined) {
        sucFun = function(data, status, headers, config) {
            console.log("success" + JSON.stringify(data));
            alert("操作成功");
        };
    }
    if (errFun == undefined) {
        errFun = function(data, status, headers, config) {
            console.log("err" + data);
            alert("请求失败！" + data);
        };
    }
    if (requestMethod) {
        requestMethod = "get";
    }
    element.click(function() {
        $http.jsonp(
            urlPath
        ).success(sucFun).error(errFun);
    });
}

function alertToast(title, text) {
    if (title == undefined) {
        swal(text);
    } else {
        swal(title, text);
    }
}

function alertConfirm(jo, callback) {
    swal(jo, callback);
}
