package com.example.fastapi.dboModel;

import java.util.Date;

public class PaymentRecord {

    private Date paymentDate;
    private Double amountPaid;

    public PaymentRecord() {}

    public PaymentRecord(Date paymentDate, Double amountPaid) {
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }
}
