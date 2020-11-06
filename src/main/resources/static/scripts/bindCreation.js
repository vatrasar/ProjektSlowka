


$("#back").on("click",function (event) {
    event.preventDefault();
    if($("#back").attr("href")==="#")
    {
        window.open("/cwicz","_self");
    }


});


function init()
{
    $.get("/getBindQuestionsForCurrentQuestion",function (datas)
    {
        $('#trInProjectGroup').html("");
        for(var question in datas)
        {
            $('#trInProjectGroup').append('<tr><td>'+datas[question].question+'</td><td>'+datas[question].answer+'</td><td class="projectSettingsTd"><a class="'+"drop"+'" href="'+datas[question].id+'">usuń</a></td></tr>');

        }
        $(".drop").on("click",function dropRow(event)
        {
            event.preventDefault();

            $(this).parent().parent().remove();
            var choosedQuestionId=$(this).attr("href");

            $.get("/dropBind?id="+choosedQuestionId);


        });
    });
}

init();


$("#search").on("input",function (event) {
    var textContent=$("#search").val();
    $.get("/searchQuestions?textContent="+textContent,function (datas) {

        $('#trGroup').html("");
        console.log("test");
        for(var question in datas)
        {
            $('#trGroup').append('<tr><td>'+datas[question].question+'</td><td>'+datas[question].answer+'</td><td class="projectSettingsTd"><a class="'+"acceptA"+'" href="'+datas[question].id+'">wybierz</a></td></tr>');

        }
        $(".acceptA").on("click",function (event) {
            event.preventDefault();
            trNumber=$("#trInProjectGroup").children().length;
            var choosedQuestionId=$(this).attr("href");
            $.get("/bindQuestions?targetQuestionId="+choosedQuestionId);
            $.get("/getQuestion?targetQuestionId="+choosedQuestionId,function (question) {
                $('#trInProjectGroup').append('<tr><td>'+question.question+'</td><td>'+question.answer+'</td><td class="projectSettingsTd"><a href="'+question.id+'">usuń</a></td></tr>');
                $("#drop"+(trNumber+1)).on("click",function dropRow(event)
                {
                    $(this).parent().parent().remove();
                    var choosedQuestionId=$(this).attr("href");




                });
            })


        })
    });
});


