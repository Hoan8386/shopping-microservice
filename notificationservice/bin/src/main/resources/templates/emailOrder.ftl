<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác nhận đơn hàng</title>
</head>

<body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif; color:#333;">

<table width="100%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; padding:40px 0;">
    <tr>
        <td align="center">

        <!-- Container -->
        <table width="600" cellpadding="0" cellspacing="0"
               style="background-color:#ffffff; border-radius:12px; overflow:hidden;">

            <!-- Header -->
            <tr>
                <td style="background-color:#2563eb; padding:30px; text-align:center;">
                    <h1 style="margin:0; color:#ffffff; font-size:28px;">
                        🛍️ SHOPPING
                    </h1>

                    <p style="margin:8px 0 0; color:#dbeafe; font-size:14px;">
                        Xác nhận đơn hàng
                    </p>
                </td>
            </tr>

            <!-- Greeting -->
            <tr>
                <td style="padding:35px 40px 20px;">
                    <h2 style="margin:0 0 12px; font-size:22px; color:#111827;">
                        Xin chào ${name}! 👋
                    </h2>

                    <p style="margin:0; font-size:15px; line-height:1.6; color:#6b7280;">
                        Cảm ơn bạn đã đặt hàng tại <strong>Shopping</strong>.
                        Đơn hàng của bạn đã được tiếp nhận thành công.
                    </p>
                </td>
            </tr>

            <!-- Order Info -->
            <tr>
                <td style="padding:10px 40px 25px;">

                    <table width="100%" cellpadding="0" cellspacing="0"
                           style="background-color:#f9fafb; border-radius:8px;">

                        <tr>
                            <td style="padding:18px;">
                                <span style="font-size:13px; color:#6b7280;">
                                    Mã đơn hàng
                                </span>

                                <br>

                                <strong style="font-size:16px; color:#2563eb;">
                                    ${orderId}
                                </strong>
                            </td>

                            <td align="right" style="padding:18px;">
                                <span style="font-size:13px; color:#6b7280;">
                                    Trạng thái
                                </span>

                                <br>

                                <strong style="font-size:14px; color:#16a34a;">
                                    ✓ Đã đặt hàng
                                </strong>
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>

            <!-- Order Details -->
            <tr>
                <td style="padding:0 40px 20px;">

                    <h3 style="margin:0 0 15px; font-size:18px; color:#111827;">
                        Chi tiết đơn hàng
                    </h3>

                    <table width="100%" cellpadding="0" cellspacing="0"
                           style="border-collapse:collapse;">

                        <!-- Table Header -->
                        <tr style="background-color:#f3f4f6;">
                            <th align="left"
                                style="padding:12px; font-size:13px; color:#4b5563;">
                                Sản phẩm
                            </th>

                            <th align="center"
                                style="padding:12px; font-size:13px; color:#4b5563;">
                                SL
                            </th>

                            <th align="right"
                                style="padding:12px; font-size:13px; color:#4b5563;">
                                Đơn giá
                            </th>
                        </tr>

                        <!-- Products -->
                        <#list items as item>
                            <tr>
                                <td style="padding:15px 12px; border-bottom:1px solid #e5e7eb; font-size:14px;">
                                    <strong style="color:#111827;">
                                        ${item.productName}
                                    </strong>
                                </td>

                                <td align="center"
                                    style="padding:15px 12px; border-bottom:1px solid #e5e7eb; font-size:14px;">
                                    ${item.quantity}
                                </td>

                                <td align="right"
                                    style="padding:15px 12px; border-bottom:1px solid #e5e7eb; font-size:14px;">
                                    ${item.price}
                                </td>
                            </tr>
                        </#list>

                    </table>

                </td>
            </tr>

            <!-- Total -->
            <tr>
                <td style="padding:10px 40px 30px;">

                    <table width="100%" cellpadding="0" cellspacing="0">

                        <tr>
                            <td style="padding-top:15px; font-size:15px; color:#6b7280;">
                                Tổng tiền
                            </td>

                            <td align="right"
                                style="padding-top:15px; font-size:24px; font-weight:bold; color:#2563eb;">
                                ${totalPrice}
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>

            <!-- Thank You -->
            <tr>
                <td style="padding:25px 40px; background-color:#f9fafb; text-align:center;">

                    <p style="margin:0 0 8px; font-size:14px; color:#4b5563;">
                        Cảm ơn bạn đã tin tưởng và mua hàng tại Shopping ❤️
                    </p>

                    <p style="margin:0; font-size:13px; color:#9ca3af;">
                        Nếu bạn có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi.
                    </p>

                </td>
            </tr>

            <!-- Footer -->
            <tr>
                <td style="padding:20px; text-align:center;">

                    <p style="margin:0; font-size:12px; color:#9ca3af;">
                        © 2026 Shopping. All rights reserved.
                    </p>

                </td>
            </tr>

        </table>

    </td>
</tr>

</table>

</body>
</html>
