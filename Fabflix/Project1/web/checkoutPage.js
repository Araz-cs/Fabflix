let checkout_form = jQuery("#checkout_form")
let cart = jQuery("#cart_table_body");
let checkoutButton = jQuery("#submitButton");

function handleList(resultData)
{
    let resultArray = resultData.split('-');
    let rowHTML = "";
    let total = 0.00;

    if(resultArray[0].length > 0) {
        for (let i = 0; i < resultArray.length - 2; i+=3) {
            rowHTML += '<tr>';
            rowHTML += '<td>' + resultArray[i + 1] + '</td>';
            rowHTML += '<td>' + resultArray[i + 2] + '</td>';
            rowHTML += '<td>' + '$5.00' + '</td>'
            rowHTML += '<td></td>'+'<td></td>' +'<td></td>';
            rowHTML += '</tr>';
            total += parseInt(resultArray[i + 2]);
        }
        let price = total * 5;
        rowHTML += '<tr class = "table-dark">';
        rowHTML += '<td>' + 'Total:' + '</td>' + '<td>' + total + '</td>';
        rowHTML += '<td>' + '$' + price.toFixed(2) + '</td>';
        rowHTML += '<td></td>'+'<td></td>' +'<td></td>';
        rowHTML += '</tr>';
    }


    cart.html("");
    cart.append(rowHTML);
}


function handleSubmit(submitForm)
{
    console.log("Success, form submitted");

    submitForm.preventDefault();

    $.ajax(
        "api/checkoutPage", {
            method: "POST",
            data: checkout_form.serialize(),
            success: function(resultData)
            {
                if(resultData == "order_success")
                    window.location.href = "confirmationPage.html"
                else if(resultData == "invalid_info")
                    alert("Invalid Info, try again!")
                else if(resultData == "cart_is_empty")
                    alert("Empty Cart, Fill it up!")
            }
        }
    );
}

checkout_form.submit(handleSubmit);

$.ajax("api/shoppingCart", {
    method: "POST",
    data:"movieID=&title=&cmd=none",
    success: handleList
});