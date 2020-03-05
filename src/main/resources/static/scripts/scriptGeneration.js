


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

$("#search").on("input",function (event) {
    var textContent=$("#search").val();
    $.ajax({
        url: "/searchChapters",
        type: "PUT",
        data: textContent,
        contentType: "application/json",
        complete: function (datas) {

            $('#trGroup').html("");
            for(var row in datas.responseJSON)
            {
                $('#trGroup').append('<tr><td>'+datas.responseJSON[row]+'</td><td class="projectSettingsTd"><a class="'+"acceptA"+'" href="'+datas.responseJSON[row]+'">wybierz</a></td></tr>');

            }
            $(".acceptA").on("click",function (event) {
                event.preventDefault();
                trNumber=$("#trInProjectGroup").children().length;
                var choosedChapter=$(this).attr("href");
                $('#trInProjectGroup').append('<tr><td>'+choosedChapter+'</td><td class="projectSettingsTd"><a id="'+"drop"+(trNumber+1)+'">Usuń</a></td><td class="projectSettingsTd"><input name="'+choosedChapter+'" type="checkbox"></td></tr>');
                $("#drop"+(trNumber+1)).on("click",function dropRow(event)
                {
                    $(this).parent().parent().remove();
                });

            })
        }
    });
});



$("#btnGenerateProject").on("click",function (event) {
    event.preventDefault();
    var selectedChapters=[];
    $("input[type=checkbox]:checked").each(function () {
      selectedChapters.push($(this).attr("name"));
    });
    var projectName=$("#projectName").val();
    var jsonProject={"projectName":projectName,"chaptersNames":selectedChapters};
    $.ajax({
        url: "/makeLatexScript",
        type: "PUT",
        data: JSON.stringify(jsonProject),
        contentType: "application/json",
        complete:function () {
            alert("doszło!")

        }});

});



