package com.macrosoft.bms.data;

public enum PaymentMethodType {

    BANK_CHECK("Bank Check"),
    BANK_VOUCHER("Bank Voucher"),
    OTHERS("Others");

    private String label;

    PaymentMethodType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }


}