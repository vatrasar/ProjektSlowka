var elements=document.getElementsByClassName("Odp");
for(var i=0;i<elements.length;i++)
{
    var elemen=elements.item(i);

    var tekst=elemen.textContent;

    if(tekst.indexOf("\n")==-1)
    {
        var parent=elemen.parentNode;
        var content=parent.innerHTML;

        parent.innerHTML=content.replace("pre","p");

    }
}