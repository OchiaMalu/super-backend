package net.zjitc.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = 1473299551300760408L;
    /**
     * id
     */
    @ApiModelProperty(value = "队伍id")
    private Long teamId;

}
