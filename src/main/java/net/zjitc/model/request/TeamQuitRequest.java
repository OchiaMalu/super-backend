package net.zjitc.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = 1473299551300760408L;
    /**
     * id
     */
    private Long teamId;

}
