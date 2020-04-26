function determineAnswerTagNames() {
    var elements=[].slice.call(document.getElementsByTagName("pre"));

    elements.forEach(function (elemen,index) {


        var tekst=elemen.textContent;

        if(tekst.indexOf("\n")===-1)
        {
            var parent=elemen.parentNode;
            var content=parent.innerHTML;
            parent.innerHTML=content.replace("pre","p");
            parent.innerHTML=content.replace("pre","p");

        }
    })
}
determineAnswerTagNames2();
/**
 * for REST API determine whether pre or p tag is use
 */
function determineAnswerTagNames2() {
    var elements=[].slice.call(document.getElementsByClassName("propAnswer"));

    elements.forEach(function (elemen,index) {


        var tekst=elemen.textContent;

        if(tekst.indexOf("\n")===-1)
        {
            $("pre.propAnswer").addClass("hidden");

            $("p.propAnswer").removeClass("hidden");

        }
        else
        {
            $("p.propAnswer").addClass("hidden");
            $("pre.propAnswer").removeClass("hidden");
            betterPreLooks();
        }
    })
}