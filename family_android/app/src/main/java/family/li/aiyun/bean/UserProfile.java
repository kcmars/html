package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/19.
 * 用户基本信息
 */

public class UserProfile implements Serializable {

    /**
     * id: 宗族id
     * user_id: 用户user_id
     * sex: 性别
     * name: 姓名
     * head_img: 头像
     * is_alive: 是否健在
     * birth_year: 出生年份
     * birth_month: 出生月份
     * birth_date: 出生日期
     * alias: 别名
     * id_card: 身份证号
     * mark: 标记
     * ancestor_id: 祖先id
     * family: 房
     * generation: 代
     * zi: 字辈
     * origin: 籍贯
     * education: 学历
     * school: 毕业院校
     * job: 职业
     * work_place: 工作地址
     * brief： 个人简介
     * pass_away_time: 去世时间
     * rank: 排行
     * company: 公司名称
     * address: 居住地址
     */

    private int id;
    private int user_id;
    private int sex;
    private String name;
    private String head_img;
    private int is_alive;
    private int birth_year;
    private int birth_month;
    private int birth_date;
    private String alias;
    private String id_card;
    private String mark;
    private int ancestor_id;
    private String family;
    private String generation;
    private String zi;
    private String origin;
    private int education;
    private String school;
    private String job;
    private String work_place;
    private String brief;
    private String pass_away_time;
    private int rank;
    private String company;
    private String address;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public int getIs_alive() {
        return is_alive;
    }

    public void setIs_alive(int is_alive) {
        this.is_alive = is_alive;
    }

    public int getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(int birth_year) {
        this.birth_year = birth_year;
    }

    public int getBirth_month() {
        return birth_month;
    }

    public void setBirth_month(int birth_month) {
        this.birth_month = birth_month;
    }

    public int getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(int birth_date) {
        this.birth_date = birth_date;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getAncestor_id() {
        return ancestor_id;
    }

    public void setAncestor_id(int ancestor_id) {
        this.ancestor_id = ancestor_id;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getZi() {
        return zi;
    }

    public void setZi(String zi) {
        this.zi = zi;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getWork_place() {
        return work_place;
    }

    public void setWork_place(String work_place) {
        this.work_place = work_place;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getPass_away_time() {
        return pass_away_time;
    }

    public void setPass_away_time(String pass_away_time) {
        this.pass_away_time = pass_away_time;
    }

    public int getEducation() {
        return education;
    }

    public void setEducation(int education) {
        this.education = education;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
