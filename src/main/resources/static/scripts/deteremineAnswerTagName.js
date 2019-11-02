function determineAnswerTagNames() {
    var elements=[].slice.call(document.getElementsByTagName("pre"));

    elements.forEach(function (elemen,index) {


        var tekst=elemen.textContent;

        if(tekst.indexOf("\n")==-1)
        {
            var parent=elemen.parentNode;
            var content=parent.innerHTML;

            parent.innerHTML=content.replace("pre","p");

        }
    })
}

/**
 * for REST API
 */
function determineAnswerTagNames2() {
    var elements=[].slice.call(document.getElementsByClassName("answers"));

    elements.forEach(function (elemen,index) {


        var tekst=elemen.textContent;

        if(tekst.indexOf("\n")===-1)
        {
            var parent=elemen.parentNode;
            var content=parent.innerHTML;

            parent.innerHTML=content.replace("pre","p");

        }
        else
        {
            var parent=elemen.parentNode;
            var content=parent.innerHTML;
            parent.innerHTML=content.replace("p","pre");
            betterPreLooks();
        }
    })
}