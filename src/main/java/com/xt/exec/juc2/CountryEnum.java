package com.xt.exec.juc2;

import lombok.Getter;

/**
 * 枚举类型
 */
public enum CountryEnum {
    One(1, "齐"), Two(2, "楚"), Three(3, "燕"), Four(4, "赵"), Five(5, "魏"), Six(6, "吴");

    @Getter private Integer retCode;
    @Getter private String retMessage;

    CountryEnum(Integer retCode, String retMessage) {
        this.retCode = retCode;
        this.retMessage = retMessage;
    }

    public static CountryEnum forEach_CountEnum(int index) {
        CountryEnum[] countryEnums = CountryEnum.values();
        for (CountryEnum ce: countryEnums) {
            if (index == ce.getRetCode()) {
                return ce;
            }
        }
        return null;
    }
}
