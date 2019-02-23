$('table').on('click',function (event) {
    event.preventDefault();
    if(event.target.className==="toMark")
    {

        $.get(event.target.getAttribute("href"),function (data) {
            $row=$(event.target.parentNode.parentNode);
            $row.children().removeClass();
            $row.children().attr("class",data);

        });
        if(event.target.textContent==="zaznacz")
            event.target.textContent="odznacz";
        else
            event.target.textContent="zaznacz";
    }


});