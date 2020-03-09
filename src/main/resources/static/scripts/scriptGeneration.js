
function openCreationPage () {

    $("#back").attr("href","scriptCreation");
    $("#firstPage").hide();
    $("#projectCreationPage").removeClass("hidden");

}

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
        //clean creation page
        $('#trInProjectGroup').html("");
        $("#projectName").val("");
    }

});

$("#btnExistingProject").on("click",function (event) {
    event.preventDefault();
    $("#back").attr("href","scriptChoosing");
    $("#firstPage").hide();
    $("#projectChoosePage").removeClass("hidden");
    $.get("/getProjectsList",function (datas) {

        for(var project in datas)
        {
            $('#trProjectListTable').append('<tr><td>'+datas[project].name+'</td><td class="projectSettingsTd"><a class="chooseProject" href="'+(datas[project].id)+'">Wybór</a></td></tr>');
        }
        $(".chooseProject").on("click",function (event) {
            event.preventDefault();
            var projectId=$(this).attr("href");
            $.get("/getLatexProject?id="+projectId,function (datas) {
                $('#trInProjectGroup').html("");
                $("#projectName").val(datas.projectName);
                trNumber=0;
                for(var chapterName in datas.chaptersNames)
                {
                    $('#trInProjectGroup').append('<tr><td>'+datas.chaptersNames[chapterName]+'</td><td class="projectSettingsTd"><a id="'+"drop"+(trNumber+1)+'">Usuń</a></td><td class="projectSettingsTd"><input name="'+datas.chaptersNames[chapterName]+'" type="checkbox"></td></tr>');
                    $("#drop"+(trNumber+1)).on("click",function dropRow(event)
                    {
                        $(this).parent().parent().remove();
                    });
                    trNumber+=1;
                }
                $("#projectChoosePage").addClass("hidden");
                openCreationPage();
                $("#btnGenerateProject").attr("name",projectId);
                
            })

        })
    });

});



$("#btnNewProject").on("click",function (event) {
    event.preventDefault();
    $('#trInProjectGroup').html("");
    $("#projectName").val("");
    openCreationPage()

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
    var jsonProject;
    if($(this).attr("name")==="")
    {
        jsonProject={"projectName":projectName,"chaptersNames":selectedChapters};
        controllerURL="/makeLatexScript";
    }else{
        jsonProject={"projectName":projectName,"chaptersNames":selectedChapters,"id":parseInt($(this).attr("name"))};
        controllerURL="/updateLatexScript";
    }

    $.ajax({
        url: controllerURL,
        type: "PUT",
        data: JSON.stringify(jsonProject),
        contentType: "application/json",
        complete:function () {
            alert("doszło!")

        }});

});



