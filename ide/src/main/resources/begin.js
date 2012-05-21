$(document).ready(function() {
	$("#aut").change(function() {
		var app_path = $("#aut option:selected")[0].value;
		getLanguages(app_path);
		$("#message.notice.message").fadeOut("slow");
		$("#message.notice.message").html("Successfully update selected app!");
		$("#message.notice.message").fadeIn("slow");

	});

	getLanguages($("#aut option:selected")[0].value);
	
	$("#message").click(function(){
		$(this).fadeOut("slow");
	});
	
	function getLanguages(path) {
		$.get("/wd/hub/ide/getLanguages", {
			app : path
		}, function(data) {
			
			var language_selector = $("#language")[0];

			while (language_selector.options.length > 0) {
				language_selector.options[0] = null;// DELETE ALL THE OPTION
				language_selector = $("#language")[0];
			}

			var langs = JSON.parse(data);
			for ( var i = 0; i < langs.length; i++) {
				language_selector.options[i] = new Option(langs[i], langs[i]);
			}
		});
	}
});