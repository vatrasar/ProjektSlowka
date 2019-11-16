$('#odp').on("input",function () {
   var text=this.value;
   text=text.split("\n");
   if(text.length>4)
    this.setAttribute("rows",text.length);
   else
       this.setAttribute("rows",4);
});