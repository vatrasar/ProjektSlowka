// if user mark that question will be only ontion, script disable input for question
$('#reversed').on("click",function () {
    if(this.checked)
    {
        $('.question').prop('disabled', true);
    }
    else
    {
        $('.question').prop('disabled', false);
    }
});