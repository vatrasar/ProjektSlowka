function convertText(text)
{
    var newText=text.replace(/e'</g,"è");
    var newText=newText.replace(/e'>/g,"é");
    var newText=newText.replace(/a'</g,"à");
    var newText=newText.replace(/i'</g,"ì");
    return newText;
}

function convertInput(input)
{
    var text=input.val();
    var covertedText=convertText(text);
    input.val(covertedText);
}

