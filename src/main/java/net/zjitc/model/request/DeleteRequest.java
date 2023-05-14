package net.zjitc.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -7428525903309954640L;
    private long id;

}
