
  var elements = document.getElementsByTagName("a");

  for(var i = 0; i < elements.length; i++) {
     elements[i].style.color = "red";
  }

  var link = document.createElement("link");
  link.setAttribute("rel", "stylesheet");
  link.setAttribute("type", "text/css");
  link.setAttribute("href", chrome.extension.getURL("covers.css"));

  document.getElementsByTagName("head")[0].appendChild(link);

  chrome.storage.local.get('blockedstrings', function(data) {
    if (typeof data.blockedstrings != 'undefined') {
  	    var result = data.blockedstrings;
  	    result.forEach(function(item, key){
  	      getNodes(item).each(function(){
  		$(this).wrap('<div class="nixit-coverwrap"></div>');
  		$(this).addClass('nixit-covered');
  	      });
  	    });
  	    var coverDiv = '<div class="nixit-coverup"></div>';
  	    $('div.nixit-coverwrap').prepend(coverDiv);
  	    $('body div.nixit-coverwrap').css({
  	      "position": "relative"
  	    });
  	    $('body div.nixit-coverup').css({
  	      "background": "red",
  	      "width": "100%",
  	      "height": "100%",
  	      "position": "absolute",
  	      "top": "0",
  	      "left": "0"
  	    });
  	}
  });

  function getNodes(text) {
      var textNodes = $(document).find(":not(iframe, script)").contents().filter(function() {
        return this.nodeType == 3 && this.textContent.indexOf(text) > -1;
      });
      return textNodes.parent().parent();
  };