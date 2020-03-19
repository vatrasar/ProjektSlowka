questionId=null;
function makeAnswerPage(data) {
    $(".propAnswer").text(data['answer']);
    $("#userAnswer").text($("odp").text());
    $("#pus").prop('checked',data["problem"]);
    var newTags=makeMediaTags([data["photos"],data['sounds'],data['videos']]);
    $("#answerMedia").html(newTags);
    determineAnswerTagNames2();
    $("#questionPage").hide();
    $("#answerPage").show();
    $("#back").show();

}

/**
 * makes tags for media(photos,films etc.). Next it is use in html method
 * @param data
 */
function makeMediaTags(data) {
    var tags="";

    tags+="<section>";
    var test=data[0];
    for (photo in data[0]) {
        tags += "<img src=\"" + data[0][photo] + "\">\n";
    }
    tags+="</section>\n";




    tags+="<section>";

    for (sound in data[1]) {
        tags += "<audio src=\"" + data[0][sound]+ "\">\n";
    }
    tags+="</section>\n";



    tags+="<section>";

    for (video in data[1]) {
        tags += "<video src=\"" + data[0][video] + "\">\n";
    }
    tags+="</section>\n";

    return tags
}


function makeQuestionPage(isFirstQuestion,data) {



    if(!isFirstQuestion)
    {
        newTags=makeMediaTags([data["photos"],data['sounds'],data['videos']]);
        $("#questionsMedia").html(newTags);
        $("#num").text(data["id"]);
        $("#question").text(data['question']);
    }
    $("odp").text("");
    $("#answerPage").hide();
    $("#questionPage").show();
    $("#back").hide();
}

makeQuestionPage(true,[]);

$("#check").on("click", function (event) {


    event.preventDefault();
    var controlerAdress=$("#formQuestion").attr("action");
    $.get(controlerAdress,function (data) {

        makeAnswerPage(data);
        determineAnswerTagNames2();
        betterPreLooks();
    });



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


            makeQuestionPage(false,data);
        });
    }
    else {
        $.get(this.formAction+"?zal=Nie Umiem",function (data) {
            if(data==="")
            {
                open("/repetitionDone","_self");
            }

            makeQuestionPage(false,data);
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

        makeQuestionPage(false,data);
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


$("#back").on("click",function (event) {
    event.preventDefault();
    makeQuestionPage(true,[]);
});

