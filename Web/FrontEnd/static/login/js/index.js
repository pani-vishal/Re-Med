 $("#login-button").click(function(event){
		 event.preventDefault();
	 
	 $('form').fadeOut(500);
	 $('.wrapper').addClass('form-success');
});

 var dropdown = $(".dropdown");
var gender_id = $("#gender");


console.log("Hello");


var dropdown = $(".dropdown");
 gender_id.click(function(){
 	dropdown.toggleClass("dropdown-display");
 });

 $(".male").on("click", function(){
  $("#gender").val("M");
  dropdown.toggleClass("dropdown-display");
});

 $(".other").on("click", function(){
  $("#gender").val("O");
  dropdown.toggleClass("dropdown-display");
});


$(".female").on("click", function(){
  $("#gender").val("F");
  dropdown.toggleClass("dropdown-display");
});

