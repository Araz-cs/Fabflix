let orderTable = jQuery("#order_table_body");

function handleConfirmation(resultData)
{
    let rowHTML = "";
    for(let i = 0; i < resultData.length; i++)
    {
        rowHTML += "<tr>";
        rowHTML += "<td>" + resultData[i]["sID"] + "</td>";
        rowHTML += "<td>" + resultData[i]["title"] + "</td>";
        rowHTML += "<td>$5.00</td>"
        rowHTML += '<td></td>'+'<td></td>' +'<td></td>';
        rowHTML += '</tr>';
    }

    let totalPrice = resultData.length * 5;

    rowHTML += "<tr class = \"table-info\">";
    rowHTML += "<td>Total:</td>";
    rowHTML += "<td> </td>";
    rowHTML += "<td>$" + totalPrice.toFixed(2) + "</td>"
    rowHTML += '<td></td>'+'<td></td>' +'<td></td>';
    rowHTML += '</tr>';

    orderTable.append(rowHTML);
}

jQuery.ajax({
    dataType: "json",
    method: "POST",
    url: "api/confirmationPage",
    success: (resultData) => handleConfirmation(resultData)
});