package net.zjitc.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -7428525903309954640L;

    @ApiModelProperty(value = "id")
    private long id;
}
