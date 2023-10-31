package com.clyl.cloudlaw.entity;


import lombok.Data;

/**
 * @author xiugou798
 */
@Data
public class Msg {
    private int id;
    private String talkId;
    private String initiator;
    private String content;
    private String createTime;
}
