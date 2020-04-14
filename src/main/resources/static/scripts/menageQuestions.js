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
    if(selected.val()==="Nowa sekcja")
    {
        var newSection = prompt("Wprowadź nazwe nowej sekcji:", selected.val());

        var $newTable=$(this).parent().parent().parent().parent().clone();

        $newTable.find(".questionRow").remove();
        $newTable.find("caption").text(newSection);
        $newTable.attr("name",newSection);



        $("#tablesContainer").append($newTable);
        $.get("/updateSection?id="+selected.attr("name")+"&selected="+newSection);

        addNewOptionToAllRows(newSection);

        var $row=$(this).parent().parent();

        $row.find("option[value='"+newSection+"']").prop('selected', true);
        $("table[name='"+newSection+"']").append($row);


    }
    else {
        var row=$(this).parent().parent();

        var $rowParrent=row.parent();

        $.get("/updateSection?id=" + selected.attr("name") + "&selected=" + selected.val());
        $("table[name='" + selected.val() + "']").append(row);
        var childrens=$rowParrent.children();
        if($rowParrent.children().length===1)
        {
            $rowParrent.parent().remove();
        }
    }


}

function addNewOptionToAllRows(newOptionName) {
   $(".sectionSelect").each(function () {
      var firstOption= $(this).children().first();
       var newOption=firstOption.clone();
       newOption.text(newOptionName);
       newOption.val(newOptionName);
       $(this).append(newOption);
   });


}
