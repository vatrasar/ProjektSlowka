var preList=document.getElementsByTagName("pre");
if(preList.length!=null)
{
    var tekst=preList[0].textContent;
    var listaLini=tekst.split("\n");
    var maxLineSize=0;
    for(var i=0;i<listaLini.length;i++)
    {
        if(listaLini[i].length>maxLineSize)
        {
            maxLineSize=listaLini[i].length;
        }
    }
    if (maxLineSize>40)
    {
        preList[0].className="preLeft"
    }




}