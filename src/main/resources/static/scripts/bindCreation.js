


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
    if($("#back").attr("href")==="scriptProjectChoosing")
    {   $("#back").attr("href","#");
        $("#firstPage").show();
        $("#projectChoosePage").addClass("hidden");
        //clean creation page
        $('#trInProjectGroup').html("");
        $("#projectName").val("");
    }

});

$("#btnExistingProject").on("click",function (event) {
    event.preventDefault();
    $("#back").attr("href","scriptProjectChoosing");
    $("#firstPage").hide();
    $("#projectChoosePage").removeClass("hidden");
    $.get("/getProjectsList",function (datas) {
        $('#trProjectListTable').html("");
        for(var project in datas)
        {
            $('#trProjectListTable').append('<tr><td>'+datas[project].name+'</td><td class="projectSettingsTd"><a href="#" class="dropProject" name="'+(datas[project].id)+'">Usuń</a></td><td class="projectSettingsTd"><a class="chooseProject" href="'+(datas[project].id)+'">Wybór</a></td></tr>');
        }



        $('.dropProject').on("click",function (event) {
            event.preventDefault();
            if(confirm("Czy napewno chcesz usunąć projekt?"))
            {

                $.ajax({
                    url: "/dropProject",
                    type: "PUT",
                    data: $(this).attr("name"),
                    contentType: "application/json",
                    complete:function () {


                    }

                });


                $(this).parent().parent().remove();
            }

        });
        $(".chooseProject").on("click",function (event) {
            event.preventDefault();
            var projectId=$(this).attr("href");
            $.get("/getLatexProject?id="+projectId,function (datas) {
                $('#trInProjectGroup').html("");
                $("#projectName").val(datas.projectName);
                trNumber=0;
                for(var chapterName in datas.chaptersNames)
                {
                    $('#trInProjectGroup').append('<tr><td>'+datas.chaptersNames[chapterName]+'</td><td class="projectSettingsTd"><a href="#" id="'+"drop"+(trNumber+1)+'">Usuń</a></td><td class="projectSettingsTd"><input checked name="'+datas.chaptersNames[chapterName]+'" type="checkbox"></td></tr>');
                    $("#drop"+(trNumber+1)).on("click",function dropRow(event)
                    {
                        event.preventDefault();
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





$("#search").on("input",function (event) {
    var textContent=$("#search").val();
    $.get("/searchQuestions?textContent="+textContent,function (datas) {

        $('#trGroup').html("");
        console.log("test");
        for(var question in datas)
        {
            $('#trGroup').append('<tr><td>'+datas[question].question+'</td><td>'+datas[question].answer+'</td><td class="projectSettingsTd"><a class="'+"acceptA"+'" href="'+datas[question].id+'">wybierz</a></td></tr>');

        }
        // $(".acceptA").on("click",function (event) {
        //     event.preventDefault();
        //     trNumber=$("#trInProjectGroup").children().length;
        //     var choosedChapter=$(this).attr("href");
        //     $('#trInProjectGroup').append('<tr><td>'+choosedChapter+'</td><td class="projectSettingsTd"><a href="#" id="'+"drop"+(trNumber+1)+'">Usuń</a></td><td class="projectSettingsTd"><input checked name="'+choosedChapter+'" type="checkbox"></td></tr>');
        //     $("#drop"+(trNumber+1)).on("click",function dropRow(event)
        //     {
        //         $(this).parent().parent().remove();
        //     });
        //
        // })
    });
});


