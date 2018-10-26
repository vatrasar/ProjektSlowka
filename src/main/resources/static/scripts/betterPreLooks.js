var preList=document.getElementsByTagName("pre");
if(preList.length!=null)
{
    for(var p=0;p<preList.length;p++) {
        var tekst = preList[p].textContent;
        var listaLini = tekst.split("\n");
        var maxLineSize = 0;
        for (var i = 0; i < listaLini.length; i++) {
            if (listaLini[i].length > maxLineSize) {
                maxLineSize = listaLini[i].length;
            }
        }
        if (maxLineSize > 40) {
            preList[p].className = "preLeft"
        }


    }

}