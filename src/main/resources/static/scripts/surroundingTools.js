$("#btnSurroundWithCode").on("click",function () {
    var textInTextArea=$("#odp").text();
    surrondingCode="*sc\n" +
        "*ec";
    $("#odp").text(textInTextArea+surrondingCode);

})