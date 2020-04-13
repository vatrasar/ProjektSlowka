function dropQuestion (event) {
    event.preventDefault();
    var addres=$(event.target).attr("href");
    $.get(addres,function () {
        $(event.target).parent().parent().remove();
    });


}
$(".dropLink").on("click",dropQuestion);
