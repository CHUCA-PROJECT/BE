package com.chuca.memberservice.domain.owner.constant;

public enum Bank {
    /*
        KB국민은행, SC제일은행, 경남은행, 광주은행,
        기업은행, 농협, 대구은행, 부산은행,
        산업은행, 수협, 신한은행, 신협,
        외환은행, 우리은행, 우체국, 전북은행,
        제주은행, 축협, 하나은행, 한국씨티은행,
        K뱅크, 카카오뱅크, 한국투자증권, 삼성증권

    */
    KOOKMIN("004"), SCB("023"), BNK("039"), KJB("034"),
    IBK("003"), NH("011"), DGB("031"), BSB("032"),
    KDB("002"), SH("007"), SHINHAN("088"), CU("048"),
    KEB("005"), WOORI("020"), POST("071"), JBB("037"),
    JEJU("035"), CH("012"), HANA("081"), CITY("027"),
    KBANK("089"), KAKAO("090"), KI("243"), SAMSUNG("240");

    private String value;

    Bank(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
