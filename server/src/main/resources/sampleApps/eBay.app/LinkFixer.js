// removes target attributes from all links
// to fix some ebay galleries tendency to open pictures
// in new windows, which is not currently supported in UIWebView
function fixLinks()
{
	var links = document.getElementsByTagName('a');

	var urlRegEx = new RegExp(); 
	urlRegEx.compile("[A-Za-z]+://[A-Za-z0-9-_]+\\.[A-Za-z0-9-_%&\?\/.=:~]+"); 
	
	for (var i=0; i < links.length; i++)
	{
		var link = links[i];
		link.removeAttribute('target');
		
		var onClick = link.getAttribute("onClick");
		if (onClick != null)
		{
			var urls = urlRegEx.exec(onClick);
			
			if (urls.length == 1)
			{
				link.setAttribute("href", urls[0]);
				link.removeAttribute('onClick');
			}
		}
	}
}

function disableInputFields()
{
	var inputs = document.getElementsByTagName('input');

	for (var i=0; i < inputs.length; i++)
	{
		var input = inputs[i];
		input.disabled = true;
	}
}