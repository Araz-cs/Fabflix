
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

let titles = getParameterByName('title');
let directors = getParameterByName('director');
let stars = getParameterByName('star');
let genreNames = getParameterByName('genre');
let years = getParameterByName('year');
let pages = getParameterByName('page');
let counter = getParameterByName('count');
let s1 = getParameterByName('s1');
let s2 = getParameterByName('s2');

function handleMovieListResult(resultData) {
    console.log("handleMovieListResult: populating Movie List table from resultData");
    // console.log(resultData[0]["CountQ"]);
    let starTableBodyElement = jQuery("#movieList_table_body");
    // let Pagination  = resultData[0]["CountQ"];
    // let formPages = Math.ceil(parseInt(Pagination, 10)/counter);
    // console.log(Pagination);
    // console.log(formPages);

    let onPage = parseInt(pages, 10);
    let nextLink = "";
    if (resultData.length > 0) {

        if (onPage > 1)
            nextLink += "<a class = \"btn btn-secondary btn-sm\" href="
                + "movieList.html?title=" + titles
                + "&director=" + directors + "&star=" + stars + "&genre=" + genreNames + "&year=" + years
                + "&page=" + (onPage - 1) + "&count=" + counter + "&s1=" + s1 + "&s2=" + s2 + ">"
                + "Prev " + " </a> ";
        else
            nextLink += "<a class = \"btn btn-secondary btn-sm\">" + "Prev " + " </a> ";
        if (onPage > 0)
            nextLink += "<a class = \"btn btn-secondary btn-sm\" href="
                + "movieList.html?title=" + titles
                + "&director=" + directors + "&star=" + stars + "&genre=" + genreNames + "&year=" + years
                + "&page=" + (onPage + 1) + "&count=" + counter + "&s1=" + s1 + "&s2=" + s2 + ">"
                + "Next" + " </a> ";

        jQuery("#upper_page").append(nextLink);
        jQuery("#lower_page").append(nextLink);
    }
    else {
        window.location.href = "movieList.html?title=" + titles
            + "&director=" + directors + "&star=" + stars + "&genre=" + genreNames + "&year=" + years
            + "&page=" + (onPage - 1) + "&count=" + counter + "&s1=" + s1 + "&s2=" + s2 + "";
    }




    // Iterate through resultData, no more than 10 entries

    for (let i = 1; i < Math.min(resultData.length); i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="singleMovie.html?id=' + resultData[i]['mid'] + '">'
            + resultData[i]["mtitle"] +     // display movie for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultData[i]["myear"] + "</th>";
        rowHTML += "<th>" + resultData[i]["mdirector"] + "</th>";
        // rowHTML += "<th>" + resultData[i]["gname"] + "</th>";

        let genres = resultData[i]["gname"].split(", ");
        let starID = resultData[i]["sid"].split(", ");
        let starName = resultData[i]["sname"].split(", ");

        let lenStar = 0;
        if (starID.length > 3)
        {
            lenStar = 3;
        }
        else
        {
            lenStar = starID.length;
        }

        rowHTML += "<th>";
        for (let i = 0; i < lenStar; i++)
        {
            if(i === lenStar - 1)
            {
                rowHTML +=  '<a href = "singleStar.html?id=' + starID[i] + '">' + starName[i] + '</a> ';

            }
            else
            {
                rowHTML +=  '<a href = "singleStar.html?id=' + starID[i] + '">' + starName[i] + '</a>, ';
            }
        }
        rowHTML += "</th>";


        let lenGenre = 0;
        if (genres > 3)
        {
            lenGenre = 3;
        }
        else
        {
            lenGenre = genres.length;
        }
        rowHTML += "<th>";
        for (let i = 0; i < lenGenre; i++){
            if(i === lenGenre-1)
            {
                rowHTML += "<a href=" + "movieList.html?title=&director=&star=&genre=" + genres[i] + "&year=&page=1&count=50&s1=rDESC&s2=tASC>" + genres[i] + "</a> ";

            }
            else
            {
                rowHTML += "<a href=" + "movieList.html?title=&director=&star=&genre=" + genres[i] + "&year=&page=1&count=50&s1=rDESC&s2=tASC>" + genres[i] + "</a>, ";
            }
        }
        rowHTML += '</th>';

        rowHTML += "<th>" + resultData[i]["rating"] + "</th>";

        rowHTML += '<th><input type="button" onClick="shoppingCart(\'' + resultData[i]["mid"] + '\', \'' + resultData[i]['mtitle'] + '\')" value = "Add" /></th>'

        rowHTML += "</tr>";

        starTableBodyElement.append(rowHTML);
    }




    let sortv1 = jQuery("#s1");
    let sortv2 = jQuery("#s2");

    let s1Click = '';
    s1Click += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=' + counter + '&s1=rASC&s2=' + s2 + '">' + 'Rating (ASC)' + '</a>';
    s1Click += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=' + counter + '&s1=rDESC&s2=' + s2 + '">' + 'Rating (DESC)' + '</a>';
    s1Click += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=' + counter + '&s1=tASC&s2=' + s2 + '">' + 'Title (ASC)' + '</a>';
    s1Click += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=' + counter + '&s1=tDESC&s2=' + s2 + '">' + 'Title (DESC)' + '</a>';

    let s2Click = '';

    s2Click += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=' + counter + '&s1=' + s1 + '&s2=rASC">' + 'Rating (ASC)' + '</a>';
    s2Click += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=' + counter + '&s1=' + s1 + '&s2=rDESC">' + 'Rating (DESC)' + '</a>';
    s2Click += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=' + counter + '&s1=' + s1 + '&s2=tASC">' + 'Title (ASC)' + '</a>';
    s2Click += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=' + counter + '&s1=' + s1 + '&s2=tDESC">' + 'Title (DESC)' + '</a>';

    let dropPages = jQuery("#dropPages");
    let finalPages = jQuery("#finalPages");
    let sequenceOfPages = "";

    sequenceOfPages += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=10&s1=' + s1 + '&s2=' + s2 + '">' + '10' + '</a>';

    sequenceOfPages += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=25&s1=' + s1 + '&s2=' + s2 + '">' + '25' + '</a>';

    sequenceOfPages += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=50&s1=' + s1 + '&s2=' + s2 + '">' + '50' + '</a>';

    sequenceOfPages += '<a class = "dropdown-item" href = "movieList.html?title=' + titles + '&director=' + directors + '&star='
        + stars + '&genre=' + genreNames + '&year=' + years + '&page='+ pages +'&count=100&s1=' + s1 + '&s2=' + s2 + '">' + '100' + '</a>';

    dropPages.append(counter);
    finalPages.append(sequenceOfPages);

    sortv1.append(s1Click);
    sortv2.append(s2Click);
}


function shoppingCart(mid, title)
{
    alert('Added ' + title + " to your cart.");
    $.ajax("api/shoppingCart", {
        method: "POST",
        data:"movieID=" + mid + "&title=" + title +"&cmd=add"
    });
}

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/movieList?title=" + titles +  "&director=" + directors + "&star=" + stars + "&genre=" + genreNames + "&year=" + years
        + "&page=" + pages + "&count=" + counter + "&s1=" + s1 + "&s2=" + s2,
    success: (resultData) => handleMovieListResult(resultData)
});

// function handleJump()
// {
//
// }




// Makes the HTTP GET request and registers on success callback function handleStarResult
// jQuery.ajax({
//     dataType: "json", // Setting return data type
//     method: "GET", // Setting request method
//     url: "api/movieList", // Setting request url, which is mapped by StarsServlet in Stars.java
//     success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
// });