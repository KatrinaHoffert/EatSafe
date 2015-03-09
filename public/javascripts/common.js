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
 * For resolution of 700px+, we need to stretch the central smallPadding (which is the main content
 * area) down to the footer. This is a pain in the ass to do in CSS without forcing more limitations
 * on us, so we'll do it with JS.
 */
function resizeMainSection() {
  if($(window).width() >= 700) {
    var topOfMainSection = $(".smallPadding").offset().top;
    var bottomOfMainSection = $(".footer").offset().top || $(document).height();

    $(".smallPadding").css("height", bottomOfMainSection - topOfMainSection + "px");
  }
}

resizeMainSection();
$(window).resize(function() {
  resizeMainSection();
});

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