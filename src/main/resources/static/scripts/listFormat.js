listFormating();

function listFormating() {
    var elements=[].slice.call(document.getElementsByClassName("propAnswer"));

    elements.forEach(function (elemen,index) {


        var tekst=elemen.innerHTML;
        var result=toListFormat(tekst);
        elemen.innerHTML=result;
        var karak=elemen.innerHTML;
        var mis=8+2;

    })
}

function toListFormat(text) {

    var linesList=text.split("\n");
    var isInCode=false;
    var outStr=text.split("\n");
    for(i=0;i<linesList.length;i++)
    {
        var line=linesList[i];
        if(!isInCode)
        {
            if(line.length===3 && line.substring(0,3)==="*sl")
            {
                isInCode=true;
                outStr[i]="<ul>"
            }
        }
        else {
            if(line.length===3 && line.substring(0,3)==="*el")
            {
                isInCode=false;
                outStr[i]="</ul>";
            }
            else if(line.length>0 && line[0]!=='\n')
            {
                outStr[i]="<li>"+line.substring(0,line.length)+"</li>";
            }
        }

    }
    if(isInCode)
    {
        outStr.push("</ul>")
    }
    return outStr.join("\n")

}