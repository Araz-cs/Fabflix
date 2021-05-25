

function handleGenreResult(resultData)
{
    if(resultData[resultData.length - 1]["userRole"] == "employee") {
        dashboardTable = "#dashboardInfo"
        let dashboardResult = jQuery(dashboardTable);
        dashboardResult.append('<a class="nav-link" href="_dashboard.html">Dashboard</a>');
    }
    let i = 0;

    let column = "#genre"
    let resultGenre = jQuery(column);
    let rowHTML = "";

    for (i = 0; i <resultData.length; i++)
    {
        rowHTML += "<a class = \"dropdown-item\" href=" +
            "movieList.html?title=&director=&star=&genre=" + resultData[i]["gname"] +
            "&year=&page=1&count=50&s1=rDESC&s2=tASC>" + resultData[i]["gname"] + "</a>"

    }
    resultGenre.append(rowHTML)

    column = "#number"
    let NumR = jQuery(column);
    rowHTML = "";

    for (i = 0; i < 10; i++)
    {
        rowHTML += "<a class = \"dropdown-item\" href=" + "movieList.html?title=" + i + "<&director=&star=&genre=&year=&page=1&count=50&s1=rDESC&s2=tASC>" + i + "</a>";
    }

    NumR.append(rowHTML)

    i = 0;

    column = "#titleAZ"
    let titleR = jQuery(column);
    rowHTML = "";

    while(i < 26)
    {
        rowHTML += "<a class = \"dropdown-item\" href=movieList.html?title=" + String.fromCharCode(65 + i)
            + "<&director=&star=&genre=&year=&page=1&count=50&s1=rDESC&s2=tASC>" + String.fromCharCode(65 + i) + "</a>"

        i++;

        if(i == 26)
            rowHTML += "<a class = \"dropdown-item\" href=" + "movieList.html?title=" + '@' + "&director=&star=&genre=&year=&page=1&count=50&s1=rDESC&s2=tASC>" + '*' + "</a>";

    }
    titleR.append(rowHTML)

}



function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")

    // TODO: if you want to check past query results first, you can do it here

    // sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
    // with the query data
    query = query.toLowerCase();

    if(window.localStorage.getItem(query) == null) {
        console.log("sending AJAX request to backend suggestion Servlet")


        jQuery.ajax({
            "method": "GET",
            // generate the request url from the query.
            // escape the query string to avoid errors caused by special characters
            "url": "api/suggestion?query=" + escape(query),
            "success": function (data) {
                // pass the data, query, and doneCallback function into the success handler
                handleLookupAjaxSuccess(data, query, doneCallback)
            },
            "error": function (errorData) {
                console.log("lookup ajax error")
                console.log(errorData)
            }
        })
    }
    else
    {
        console.log("Query already cached, requesting data from LocalStorage");
        handlePrevResult(query, doneCallback);
    }

}

function handleLookupAjaxSuccess(data, query, doneCallback) {
    console.log("lookup ajax successful")

    // parse the string into JSON
    window.localStorage.setItem(query, data);
    var jsonData = JSON.parse(data);
    console.log(jsonData)

    // TODO: if you want to cache the result into a global variable you can do it here

    // call the callback function provided by the autocomplete library
    // add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    doneCallback( { suggestions: jsonData } );
}

function handlePrevResult(query, doneCallback)
{
    console.log("Data request to LocalStorage successful: ");
    // console.log(storage.getItem(query));
    let jsonData = JSON.parse(window.localStorage.getItem(query));

    doneCallback( { suggestions: jsonData } );
}

function handleSelectSuggestion(suggestion) {
    // TODO: jump to the specific result page based on the selected suggestion //check

    console.log("you selected" + suggestion["value"] + " for id " + suggestion["data"])
    window.location.href = "singleMovie.html?id=" + suggestion["data"];
}


$('#autocomplete').autocomplete({
    // documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
        handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters //check
    minChars: 3
});


function handleNormalSearch(query) {
    console.log("doing normal search with query: " + query);
    // TODO: you should do normal search here  //check
    window.location.href = 'movieList.html?title='+ escape(query) + '&director=&star=&genre=&year=&page=1&count=50&s1=rDESC&s2=tASC'


}

// bind pressing enter key to a handler function
$('#autocomplete').keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        handleNormalSearch($('#autocomplete').val())
    }
})

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/mainPage",
    success: (resultData) => handleGenreResult(resultData)
});