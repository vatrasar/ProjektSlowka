
$("#back").on("click",function (event) {
    event.preventDefault();
    if($("#back").attr("href")==="#")
    {
        window.open("/pokarzMenu","_self");
    }
    if($("#back").attr("href")==="scriptCreation")
    {   $("#back").attr("href","#");
        $("#firstPage").show();
        $("#projectCreationPage").addClass("hidden");
    }

});
$("#btnNewProject").on("click",function (event) {
    event.preventDefault();
    $("#back").attr("href","scriptCreation");
    $("#firstPage").hide();
    $("#projectCreationPage").removeClass("hidden");

});
