$(function() {
    var jsParams = eval("(" + window.jsparams.getJson() + ")");
    window.console.log(jsParams[0].name);
})
