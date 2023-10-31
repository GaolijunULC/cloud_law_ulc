package com.clyl.cloudlaw.entity.detail;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xiugou798
 */
@Data
public class LawyerDetail {
    private int id;
    private String lawName;
    private Integer age;
    private String phone;
    private String gender;
    private String area;
    private String experience;
    private String caseResults;
    private String cost;
    private String headImg;
    private Integer serviceNum;
    private LocalDateTime createTime;
}
