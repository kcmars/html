package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/13.
 */

public class CountryCode implements Serializable {

    /**
     "id": 1,
     "english": "Angola",
     "chinese": "安哥拉",
     "abbreviation": "AO"
     "code": 244
     "letter": "A"
     */

    private int id;
    private String english;
    private String chinese;
    private String abbreviation;
    private int code;
    private String letter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
