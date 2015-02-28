/**
 * This is for JS that is commonly used around our application.
 *
 * This file is part of EatSafe Saskatchewan.
 */

/** Used by createPageError() for storing the timer. */
var timeoutForPageError;

/** Used by createPageError() to show when the error is being removed (but is still there). */
var topErrorIsBeingHidden = false;

/**
 * Displays an error message at the top of the user's viewport. This will last 3 seconds before
 * disappearing on its own. Only one of these can be created at a time. Attempting to create another
 * wilk just overwrite the message and reset the timeout.
 *
 * @param errorMessage Plain text message to display.
 */
function createPageError(errorMessage) {
  // Creates the timer that will hide the error
  function createTimeout() {
    return window.setTimeout(function() {
      topErrorIsBeingHidden = false;
      $(".topViewError").fadeAndRemove("slow", function() {
        topErrorIsBeingHidden = false;
      });
    }, 3000);
  }

  // Check if error is already being displayed. If so, just change the text and reset the timer.
  // But if the error is currently being hidden, it's too late to stop the timer -- create a new
  // one.
  if(!topErrorIsBeingHidden || $(".topViewError").length == 0) {
    window.clearTimeout(timeoutForPageError);

    var errorHtml = $("<div>").hide();
    errorHtml.addClass("topViewError");
    errorHtml.text(errorMessage);

    $(document.body).append(errorHtml);

    timeoutForPageError = createTimeout();
    $(".topViewError").fadeIn("slow");
  }
  else {
    window.clearTimeout(timeoutForPageError);
    errorHtml.text(errorMessage);
    timeoutForPageError = createTimeout();
  }
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
    if(typeof(callback) == "function") {
      callback(this);
    }
    else {
      console.log("Callback is not a function");
      console.trace();
    }
  });
}