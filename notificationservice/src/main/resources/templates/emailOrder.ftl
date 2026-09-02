<html>
<body>

<h2>Xin chào ${name}!</h2>

<p>Cảm ơn bạn đã đặt hàng.</p>

<p>
    Mã đơn hàng:
    <strong>${orderId}</strong>
</p>

<h3>Chi tiết đơn hàng:</h3>

<table>
    <#list items as item>
        <tr>
            <td>${item.productName}</td>
            <td>${item.quantity}</td>
            <td>${item.price}</td>
        </tr>
    </#list>
</table>

<p>
    Tổng tiền:
    <strong>${totalPrice}</strong>
</p>

<p>Cảm ơn bạn đã mua hàng!</p>

</body>
</html>