package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/18.
 * 权限人员列表
 */

public class PermissionRecords implements Serializable {

    /**
     * family_supervisors: 近亲
     * clan_supervisors: 族亲
     */

    private List<PermissionMember> clan_supervisors;
    private List<PermissionMember> family_supervisors;

    public List<PermissionMember> getClan_supervisors() {
        return clan_supervisors;
    }

    public void setClan_supervisors(List<PermissionMember> clan_supervisors) {
        this.clan_supervisors = clan_supervisors;
    }

    public List<PermissionMember> getFamily_supervisors() {
        return family_supervisors;
    }

    public void setFamily_supervisors(List<PermissionMember> family_supervisors) {
        this.family_supervisors = family_supervisors;
    }
}
