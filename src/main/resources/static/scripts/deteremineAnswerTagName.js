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


        var tekst=elemen.innerHTML;
        elemen.innerHTML=soroundStar(tekst);

    })
}


function soroundStar(text) {
    var linesList=text.split("\n");
    var isInCode=false;
    var outStr=text.split("\n");
    for(i=0;i<linesList.length;i++)
    {
        var line=linesList[i];
        if(!isInCode)
        {
            if(line.length===3 && line.substring(0,3)==="*sc")
            {
                isInCode=true;
                outStr[i]="<pre>"
            }
        }
        else {
            if(line.length===3 && line.substring(0,3)==="*ec")
            {
                isInCode=false;
                outStr[i]="</pre>"
            }
        }

    }
    if(isInCode)
    {
        outStr.push("</pre>")
    }
    return outStr.join("\n")
}