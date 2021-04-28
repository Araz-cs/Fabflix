function handleGenreResult(resultData)
{
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

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/mainPage",
    success: (resultData) => handleGenreResult(resultData)
});