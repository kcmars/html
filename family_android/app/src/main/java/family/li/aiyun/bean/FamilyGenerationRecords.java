package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/22.
 * 房代信息
 */

public class FamilyGenerationRecords implements Serializable {

    /**
     * family: [] 房
     * generation: [] 代
     */

    private List<FamilyGenerationInfo> family;
    private List<FamilyGenerationInfo> generation;

    public List<FamilyGenerationInfo> getFamily() {
        return family;
    }

    public void setFamily(List<FamilyGenerationInfo> family) {
        this.family = family;
    }

    public List<FamilyGenerationInfo> getGeneration() {
        return generation;
    }

    public void setGeneration(List<FamilyGenerationInfo> generation) {
        this.generation = generation;
    }
}
