package net.zjitc.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel(value = "删除请求")
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -7428525903309954640L;

    @ApiModelProperty(value = "id")
    private long id;
}
