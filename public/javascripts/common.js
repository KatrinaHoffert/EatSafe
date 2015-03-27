/**
 * This is for JS that is commonly used around our application.
 *
 * This file is part of EatSafe Saskatchewan.
 */

// Create fake console if one doesn't exist. For compatibility with dumbass HtmlUnit.
if(typeof console == "undefined") {
  window.console = {
    log: function () {},
    warn: function () {},
    error: function () {},
    trace: function () {}
  };
}

/**
 * Displays an error message at the top of the user's viewport. This will last ~3 seconds before
 * disappearing on its own. Only one of these can be displayed at a time. Attempting to create another
 * will remove the previous.
 *
 * @param errorMessage Message to display.
 */
function createPageError(errorMessage) {
  // Destroy any existing errors
  $(".topViewError").fadeAndRemove("fast");

  var errorHtml = $('<div class="alert alert-danger alert-dismissible topViewError" role="alert" data-dismiss="alert">' +
      errorMessage + '</div>').hide();

  $(document.body).append(errorHtml);

  var errorDiv = $(".topViewError");
  errorDiv.fadeIn("slow", function() {
    window.setTimeout(function() {
      errorDiv.fadeAndRemove("slow");
    }, 3000);
  });
}

/**
 * Fades and then removes a DOM element once it has faded out.
 * @param duration The duration of the fade. Same settings as fadeOut.
 * @param callback A function called after the removal is done.
 */
jQuery.fn.fadeAndRemove = function(duration, callback) {
  this.fadeOut(duration || "slow", function() {
    this.remove();
    
    // Sane error handling: provide stack traces for the dumb schmuck who did this.
    if(callback != null && typeof(callback) == "function") {
      callback(this);
    }
    else {
      console.log("Callback is not a function");
      console.trace();
    }
  });
}

/**
 * String formatting for JS. Replaces {0}, {1}, etc with the numbered argument.
 *
 * Example:
 *   "Hello, {0}, I am {1}!".format("Mike", "Joe the Plumber")
 * Results in:
 *   "Hello, Mike, I am Joe the Plumber!"
 *
 * @return A new string with the replacements made.
 */
String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) { 
    return typeof args[number] != 'undefined'
      ? args[number]
      : match
    ;
  });
};


// Konami code for shits and giggles. Only for desktop users because the background makes text
// unreadable without the extra div desktop has. Not that mobile users really have a way to
// type this.
cheet("↑ ↑ ↓ ↓ ← → ← → b a", function() {
  if($(window).width() >= 700) {
    $("body").css("background-image", "url(\"assets/images/mmm_pizza.jpg\")");
    $("body").css("background-attachment", "fixed");
    $("body").css("background-size", "cover");
    $("body").css(" background-position", "center");
  }
});