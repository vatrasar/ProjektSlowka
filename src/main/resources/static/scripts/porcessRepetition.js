
function makeAnswerPage() {
    $("#questionPage").hide();
    $("#answerPage").show();

}

function makeQuestionPage() {
    $("odp").text("");
    $("#answerPage").hide();
    $("#questionPage").show();
}

makeQuestionPage();

$("#check").on("click", function (event) {

    makeAnswerPage();
    event.preventDefault();
    var controlerAdress=$("#formQuestion").attr("action");
    $.get(controlerAdress,function (data) {
        $("#propAnswer").text(data['answer']);
        $("#userAnswer").text($("odp").text());
        $("#pus").checked=data["problem"];
    });
    determineAnswerTagNames();
    betterPreLooks();

});

$(".result").on("click",function (event) {
    event.preventDefault();
    makeQuestionPage();
    if(this.value==="Umiem")
    {
        $.get(this.formAction+"?zal=Umiem",function (data) {


            if(data==="")
            {
                open("/repetitionDone");
            }
            $("#question").text(data['question']);

            $("#num").text(data["id"]);

        });
    }
    else {
        $.get(this.formAction+"?zal=Nie Umiem",function (data) {
            if(data===null)
            {
                open("/repetitionDone");
            }
            $("#num").text(data["id"]);
            $("#question").text(data['question']);

        });
    }


});
