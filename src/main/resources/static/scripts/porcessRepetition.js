questionId=null;
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


    event.preventDefault();
    var controlerAdress=$("#formQuestion").attr("action");
    $.get(controlerAdress,function (data) {
        $("#propAnswer").text(data['answer']);
        $("#userAnswer").text($("odp").text());
        $("#pus").checked=data["problem"];
        makeAnswerPage();
    });

    determineAnswerTagNames();
    betterPreLooks();

});

$(".result").on("click",function (event) {
    event.preventDefault();
    questionId=true;
    if(this.value==="Umiem")
    {
        $.get(this.formAction+"?zal=Umiem",function (data) {


            if(data==="")
            {
                open("/repetitionDone","_self");
            }
            $("#question").text(data['question']);

            $("#num").text(data["id"]);
            makeQuestionPage();
        });
    }
    else {
        $.get(this.formAction+"?zal=Nie Umiem",function (data) {
            if(data==="")
            {
                open("/repetitionDone","_self");
            }
            $("#num").text(data["id"]);
            $("#question").text(data['question']);
            makeQuestionPage();
        });
    }



});

function workNext()
{
    $.get("/cwiczNext",function (data) {
        if(data==="")
        {
            open("/repetitionDone","_self");
        }
        $("#num").text(data["id"]);
        $("#question").text(data['question']);
        makeQuestionPage();
    });
}

$("#pus").on("click",function () {
    $.get('/toggleProb');
});

$("#drop").on("click",function (event) {
    event.preventDefault();

    $.get('/dropPytanie?id=-1',workNext);



});


$("#leave").on("click",function (event) {

   if (confirm("Czy napewno chcesz opuścić?"))
   {

   }else {
       event.preventDefault()
   }



});

$("#questionChange").on("click",function (event) {
    event.preventDefault();
    open('/zmianaPytania?id=-1',"_self");
});


