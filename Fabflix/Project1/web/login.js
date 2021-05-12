let login_form = $("#login_form");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);
    console.log(resultDataJson);
    if(resultDataJson["recaptchaStatus"] === "success") {
        console.log(resultDataJson["recaptchaStatus"]);
        console.log("handle login response");
        console.log(resultDataJson);
        console.log(resultDataJson["status"]);


        if (resultDataJson["status"] === "success") {
            // if (resultDataString.indexOf("success") > 0) {
            window.location.replace("mainPage.html");
        } else {
            // If login fails, the web page will display
            // error messages on <div> with id "login_error_message"
            // console.log("show error message");
            //alert("wrong username or password");
            $("#login_error_message").text("wrong username or password");
            // location.reload();
            alert("Invalid information, try again please!");
            window.reload(true);

        }
    }
    else{
        // location.reload();
        alert("reCAPTCHA verification failed, try again please!")
        window.reload(true);

    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/login", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: login_form.serialize(),
            success: handleLoginResult
        }
    );
}

// Bind the submit action of the form to a handler function
login_form.submit(submitLoginForm);

