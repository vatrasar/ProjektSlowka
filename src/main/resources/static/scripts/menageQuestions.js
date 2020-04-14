function dropQuestion (event) {
    event.preventDefault();
    var addres=$(event.target).attr("href");
    $.get(addres,function () {
        $(event.target).parent().parent().remove();
    });


}
$(".dropLink").on("click",dropQuestion);

$(".sectionSelect").on("change",afterChangeOption);

function afterChangeOption() {
    var selected = $(this).children("option:selected");
    var row=$(this).parent().parent();
    $.get("/updateSection?id="+selected.attr("name")+"&selected="+selected.val());
    $("table[name='"+selected.val()+"']").append(row);


}
