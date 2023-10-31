package com.clyl.cloudlaw.service;

import com.clyl.cloudlaw.entity.Lawyer;
import com.clyl.cloudlaw.entity.resp.RestBean;

import java.util.List;

/**
 * @author xiugou798
 */
public interface LawyerService {
    RestBean<List<Lawyer>> getAllLawyers();

    RestBean<List<Lawyer>> getLawyers();

    RestBean<Object> add(Lawyer lawyer);
}
