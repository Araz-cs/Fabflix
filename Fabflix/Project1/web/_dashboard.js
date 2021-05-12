let star_keywords = $("#star_keywords");
let movie_keywords = $("#movie_keywords");


function submitStarForm(formSubmitEvent) {

    formSubmitEvent.preventDefault();
    console.log(star_keywords.serialize());
    $.ajax(
        "api/Dashboard?" + star_keywords.serialize() , {
            method: "POST",
            success: function(resultData)
            {
                let json = JSON.parse(resultData);
                alert(json["message"]);
            }
        }
    );
}


function submitMovieForm(formSubmitEvent) {

    formSubmitEvent.preventDefault();
    console.log(movie_keywords.serialize());
    $.ajax(
        "api/Dashboard?" +  movie_keywords.serialize(), {
            method: "POST",
            success: function(resultData)
            {
                let json = JSON.parse(resultData);
                alert(json["message"]);
            }
        }
    );
}

function handleDatabaseResult(resultData) {
    if (resultData[resultData.length - 1]["userRole"] != "employee")
        window.history.back();
    else{
        let database = jQuery("#database");
        let rawHTML = "";

        for(let i = 0; i < resultData.length - 1 ; i++){
            var tableResults = resultData[i].result.split(",")

            rawHTML += '<tr>';
            rawHTML += '<th >' + resultData[i]['table_name'] + '</th>';
            for(let j = 0; j <  tableResults.length - 1 ; j++){

                rawHTML += '<td>';
                rawHTML += '<td>' + tableResults[j] + '</td>';
                rawHTML += '</td>';
            }
            rawHTML += '</tr>';

        }
        database.append(rawHTML);
    }

}
jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/Dashboard",
    success: (resultData) => handleDatabaseResult(resultData)
});

star_keywords.submit(submitStarForm);
movie_keywords.submit(submitMovieForm);