package com.clyl.cloudlaw.entity;

import lombok.Data;

/**
 * @author xiugou798
 */
@Data
public class Talk {
    private int id;
    private String talkId;
    private String userId;
    private String title;
    private Integer talkCount;
    private String createTime;
    private String updateTime;
    private String isDelete;
    private String deleteTime;
}
