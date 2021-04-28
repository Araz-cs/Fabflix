let cart = jQuery("#cart_table_body");
let checkout = jQuery("#checkout");

function handleCartData(resultData)
{
    let resultArray = resultData.split('-');
    let rawHTML = "";
    let total = 0.00;

    if(resultArray[0].length > 0) {
        for (let i = 0; i <= resultArray.length - 1; i+=3) {
            rawHTML += '<tr>';
            rawHTML += '<td>' + resultArray[i + 1] + '</td>';
            rawHTML += '<td>' + resultArray[i + 2] + '</td>';
            rawHTML += '<td>' + '$5.00' + '</td>';
            rawHTML += '<td>' + '<div onClick="updateShoppingCart(\'' + resultArray[i] + '\', \'' + resultArray[i + 1] + '\', \'add\')" > Add </div>' + '</td>';
            rawHTML += '<td>' + '<div onClick="updateShoppingCart(\'' + resultArray[i] + '\', \'' + resultArray[i + 1] + '\', \'sub\' )" > Remove </div>' + '</td>';
            rawHTML += '<td>' + '<div onClick="updateShoppingCart(\'' + resultArray[i] + '\', \'' + resultArray[i + 1] + '\', \'del\')" > Delete </div>' + '</td>';
            rawHTML += '</tr>'

            total += parseInt(resultArray[i + 2]);
        }

        let price = total * 5;
        rawHTML += '<tr class = "table-dark">';
        rawHTML += '<td>' + 'Total:' + '</td>';
        rawHTML += '<td>' + total + '</td>';
        rawHTML += '<td>' + '$' + price.toFixed(2) + '</td>';
        rawHTML += '<td></td>'+'<td></td>' +'<td></td>';
        rawHTML += '</tr>'
    }

    cart.html("");
    checkout.html("");
    cart.append(rawHTML);

    if(resultArray[0].length > 0)
        checkout.append("<a href = \"checkoutPage.html\" > Check Me Out </a>");
}

function updateShoppingCart(movieID, title, cmd)
{
    $.ajax("api/shoppingCart", {
        method: "POST",
        data:"movieID=" + movieID + "&title=" + title + "&cmd=" + cmd,
        success: handleCartData
    });
}

$.ajax("api/shoppingCart", {
    method: "POST",
    data:"movieID=&title=&cmd=none",
    success: handleCartData
});