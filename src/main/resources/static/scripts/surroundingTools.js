$("#btnSurroundWithCode").on("click",function () {
    var textInTextArea=$("#odp").val();
    surrondingCode="*sc\n" +
        "*ec";
    $("#odp").val(textInTextArea+surrondingCode);

})