package family.li.aiyun.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 家族人员
 */

public class FamilyMember implements Serializable {

    private Person self = null; //自己信息
    private Person spouse = null;//配偶
    private List<FamilyMember> children = new ArrayList<>();//儿女

    public Person getSelf() {
        return self;
    }

    public void setSelf(Person self) {
        this.self = self;
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public List<FamilyMember> getChildren() {
        return children;
    }

    public void setChildren(List<FamilyMember> children) {
        this.children = children;
    }
}
