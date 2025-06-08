package com.zosh.mapper;

import com.zosh.dto.PaymentDetailsDTO;
import com.zosh.modal.PaymentDetails;

public class PaymentDetailsMapper {

    public static PaymentDetailsDTO toDTO(PaymentDetails details) {
        if (details == null) return null;
        PaymentDetailsDTO dto = new PaymentDetailsDTO();
        dto.setPaymentMethod(details.getPaymentMethod());
        dto.setStatus(details.getStatus());
        dto.setPaymentId(details.getPaymentId());
        dto.setRazorpayPaymentLinkId(details.getRazorpayPaymentLinkId());
        dto.setRazorpayPaymentLinkReferenceId(details.getRazorpayPaymentLinkReferenceId());
        dto.setRazorpayPaymentLinkStatus(details.getRazorpayPaymentLinkStatus());
        dto.setRazorpayPaymentId(details.getRazorpayPaymentId​()); // NOTE: check strange character issue
        return dto;
    }

    public static PaymentDetails toEntity(PaymentDetailsDTO dto) {
        if (dto == null) return null;
        PaymentDetails details = new PaymentDetails();
        details.setPaymentMethod(dto.getPaymentMethod());
        details.setStatus(dto.getStatus());
        details.setPaymentId(dto.getPaymentId());
        details.setRazorpayPaymentLinkId(dto.getRazorpayPaymentLinkId());
        details.setRazorpayPaymentLinkReferenceId(dto.getRazorpayPaymentLinkReferenceId());
        details.setRazorpayPaymentLinkStatus(dto.getRazorpayPaymentLinkStatus());
        details.setRazorpayPaymentId​(dto.getRazorpayPaymentId()); // NOTE: see below
        return details;
    }
}
