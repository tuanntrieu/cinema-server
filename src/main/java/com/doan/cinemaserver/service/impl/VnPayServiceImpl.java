package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.config.VnPayConfig;
import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.domain.dto.vnpay.PaymentResponseDTO;
import com.doan.cinemaserver.domain.dto.vnpay.PaymentStatusResponse;
import com.doan.cinemaserver.exception.InvalidException;
import com.doan.cinemaserver.service.VnPayService;
import com.doan.cinemaserver.util.PaymentUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VnPayServiceImpl implements VnPayService {

    private static final Map<String, String> RESPONSE_CODE_MESSAGES = Map.ofEntries(
            Map.entry("00", "Giao dịch thành công"),
            Map.entry("07", "Trừ tiền thành công. Giao dịch bị nghi ngờ"),
            Map.entry("09", "Thẻ/Tài khoản chưa đăng ký InternetBanking"),
            Map.entry("10", "Xác thực thông tin sai quá 3 lần"),
            Map.entry("11", "Hết thời gian thanh toán"),
            Map.entry("12", "Thẻ/Tài khoản bị khóa"),
            Map.entry("13", "Sai OTP"),
            Map.entry("24", "Khách hàng hủy giao dịch"),
            Map.entry("51", "Không đủ số dư"),
            Map.entry("65", "Vượt hạn mức giao dịch trong ngày"),
            Map.entry("75", "Ngân hàng bảo trì"),
            Map.entry("79", "Sai mật khẩu thanh toán quá số lần"),
            Map.entry("99", "Lỗi khác")
    );

    private final VnPayConfig vnPayConfig;

    @Override
    public PaymentResponseDTO createPaymentUrl(Long amount, HttpServletRequest request) {

        String vnp_Version = vnPayConfig.getVersion();
        String vnp_Command = vnPayConfig.getCommand();
        String vnp_TmnCode = vnPayConfig.getTmnCode();

        String vnp_TxnRef = UUID.randomUUID().toString().replace("-", "");
        String vnp_IpAddr = PaymentUtil.getIpAddress(request);

        Map<String, String> vnp_Params = new HashMap<>();

        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
        if (vnp_OrderInfo == null || vnp_OrderInfo.isEmpty()) {
            vnp_OrderInfo = "Thanh toan don hang: " + vnp_TxnRef;
        }
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);

        String orderType = request.getParameter("ordertype");
        if (orderType == null || orderType.isEmpty()) {
            orderType = vnPayConfig.getOrderType();
        }
        vnp_Params.put("vnp_OrderType", orderType);

        String bank_code = request.getParameter("bankcode");
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }

        String locate = request.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }

        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        /* Billing */

        String billMobile = request.getParameter("txt_billing_mobile");
        if (billMobile != null && !billMobile.isEmpty()) {
            vnp_Params.put("vnp_Bill_Mobile", billMobile);
        }

        String billEmail = request.getParameter("txt_billing_email");
        if (billEmail != null && !billEmail.isEmpty()) {
            vnp_Params.put("vnp_Bill_Email", billEmail);
        }

        String fullName = request.getParameter("txt_billing_fullname");
        if (fullName != null && !fullName.isEmpty()) {
            fullName = fullName.trim();
            int idx = fullName.indexOf(' ');
            if (idx > 0) {
                String firstName = fullName.substring(0, idx);
                String lastName = fullName.substring(fullName.lastIndexOf(' ') + 1);
                vnp_Params.put("vnp_Bill_FirstName", firstName);
                vnp_Params.put("vnp_Bill_LastName", lastName);
            }
        }

        String address = request.getParameter("txt_inv_addr1");
        if (address != null && !address.isEmpty()) {
            vnp_Params.put("vnp_Bill_Address", address);
        }

        String city = request.getParameter("txt_bill_city");
        if (city != null && !city.isEmpty()) {
            vnp_Params.put("vnp_Bill_City", city);
        }

        String country = request.getParameter("txt_bill_country");
        if (country != null && !country.isEmpty()) {
            vnp_Params.put("vnp_Bill_Country", country);
        }

        String state = request.getParameter("txt_bill_state");
        if (state != null && !state.isEmpty()) {
            vnp_Params.put("vnp_Bill_State", state);
        }

        /* Invoice */

        String invPhone = request.getParameter("txt_inv_mobile");
        if (invPhone != null && !invPhone.isEmpty()) {
            vnp_Params.put("vnp_Inv_Phone", invPhone);
        }

        String invEmail = request.getParameter("txt_inv_email");
        if (invEmail != null && !invEmail.isEmpty()) {
            vnp_Params.put("vnp_Inv_Email", invEmail);
        }

        String invCustomer = request.getParameter("txt_inv_customer");
        if (invCustomer != null && !invCustomer.isEmpty()) {
            vnp_Params.put("vnp_Inv_Customer", invCustomer);
        }

        String invAddress = request.getParameter("txt_inv_addr1");
        if (invAddress != null && !invAddress.isEmpty()) {
            vnp_Params.put("vnp_Inv_Address", invAddress);
        }

        String invCompany = request.getParameter("txt_inv_company");
        if (invCompany != null && !invCompany.isEmpty()) {
            vnp_Params.put("vnp_Inv_Company", invCompany);
        }

        String invTax = request.getParameter("txt_inv_taxcode");
        if (invTax != null && !invTax.isEmpty()) {
            vnp_Params.put("vnp_Inv_Taxcode", invTax);
        }

        String invType = request.getParameter("cbo_inv_type");
        if (invType != null && !invType.isEmpty()) {
            vnp_Params.put("vnp_Inv_Type", invType);
        }

        /* Build hash */

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        int i = 0;

        for (String fieldName : fieldNames) {

            String fieldValue = vnp_Params.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {

                if (i > 0) {
                    hashData.append("&");
                    query.append("&");
                }

                hashData.append(fieldName)
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                i++;
            }
        }

        String queryUrl = query.toString();

        String vnp_SecureHash = PaymentUtil.hmacSHA512(
                vnPayConfig.getHashSecret(),
                hashData.toString()
        );

        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        String paymentUrl = vnPayConfig.getPayUrl() + "?" + queryUrl;

        return PaymentResponseDTO.builder()
                .status("ok")
                .message("success")
                .URL(paymentUrl)
                .build();
    }

    @Override
    public PaymentStatusResponse handleVNPayReturn(String receivedHash, HttpServletRequest request) {

        // Lấy tất cả param từ request
        Map<String, String> parameters = PaymentUtil.getParametersFromRequest(request);

        // Lấy secure hash VNPay gửi
        String vnpSecureHash = parameters.remove("vnp_SecureHash");

        // Remove hash type (không được đưa vào hashData)
        parameters.remove("vnp_SecureHashType");

        // Build lại chuỗi hashData
        String hashData = PaymentUtil.buildHashData(parameters);

        // Tính lại chữ ký
        String calculatedHash = PaymentUtil.hmacSHA512(
                vnPayConfig.getHashSecret(),
                hashData
        );

        // So sánh chữ ký
        if (!vnpSecureHash.equalsIgnoreCase(calculatedHash)) {
            throw new InvalidException(ErrorMessage.Payment.ERR_INVALID_OR_TAMPERED_DATA);
        }

        // Lấy mã phản hồi VNPay
        String vnpResponseCode = parameters.get("vnp_ResponseCode");

        String message = RESPONSE_CODE_MESSAGES.getOrDefault(
                vnpResponseCode,
                "Không xác định mã lỗi"
        );

        return new PaymentStatusResponse(
                message,
                "00".equals(vnpResponseCode)
        );
    }
}