function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


function handleResult(resultData) {

    let addButton = jQuery("#shopButton");
    addButton.append('<input class = "btn btn-secondary" type="button" onClick="shoppingCart(\'' + resultData[0]["mID"] + '\', \'' + resultData[0]['title']+ '\')" value = "Add to Cart" />');

    let URLnow = resultData[0]["curURL"];
    let jumpFunc = jQuery("#jump_func");
    jumpFunc.append('<a class="nav-link" href = "' + URLnow + '">Movie List</a>')

    console.log("handleResult: populating star info from resultData");


    let starInfoElement = jQuery("#movie_info");

    starInfoElement.append("<p>" + resultData[1]["title"] + "</p>" +
        "<p>Directors: " + resultData[1]["director"] + "</p>" +
        "<p>Genre: " + resultData[1]["genres"] + "</p>" +
        "<p>Year: " + resultData[1]["year"] + "</p>"+
        "<p>Rating: " + resultData[1]["rating"] + "</p>");

    console.log("handleResult: populating movie table from resultData");


    let movieTableBodyElement = jQuery("#movie_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows

    for (let i = 1; i < resultData.length; i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" +
            // Add a link to singleMovie.html with id passed with GET url parameter
            '<a href="singleStar.html?id=' + resultData[i]['smID'] + '">'
            + resultData[i]['stars'] + // display movie for the link text
            '</a>' +
            "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}


function shoppingCart(mid, title)
{
    alert('Added ' + title + " to your cart.");
    $.ajax("api/shoppingCart", {
        method: "POST",
        data:"movieID=" + mid + "&title=" + title +"&cmd=add"
    });
}

// Get id from URL
let movieId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/singleMovie?id=" + movieId, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});