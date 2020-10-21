$(document).ready(function(){
    $("area").off('click').on('click', function(){
    //点击以后的JS->android ,调用Android中JavaScriptInterface.station()
        JavaScriptInterface.station($(this).prop('alt'));
    });
})