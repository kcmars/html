package com.keyc.mycustomview.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/13.
 */

public class CountryCode implements Serializable {

    /**
     "code": "AND",
     "number": "020",
     "english": "Andorra",
     "chinese": "安道尔"
     */

    private String code;
    private String number;
    private String english;
    private String chinese;
    private String letter;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
