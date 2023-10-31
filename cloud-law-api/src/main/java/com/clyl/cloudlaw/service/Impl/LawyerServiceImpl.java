package com.clyl.cloudlaw.service.Impl;

import com.clyl.cloudlaw.entity.Lawyer;
import com.clyl.cloudlaw.entity.Msg;
import com.clyl.cloudlaw.entity.resp.RestBean;
import com.clyl.cloudlaw.mapper.LawyerMapper;
import com.clyl.cloudlaw.service.LawyerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiugou798
 */
@Service
public class LawyerServiceImpl implements LawyerService {

    @Resource
    private LawyerMapper lawyerMapper;

    @Override
    public RestBean<List<Lawyer>> getAllLawyers() {
        List<Lawyer> lawyers = lawyerMapper.getAllLawyers();
        return lawyers.size() > 0
                ? new RestBean<>(200, "查询成功", lawyers)
                : new RestBean<>(401, "已经没有数据了", null);
    }

    @Override
    public RestBean<List<Lawyer>> getLawyers() {
        List<Lawyer> lawyers = lawyerMapper.getLawyers();
        return lawyers.size() > 0
                ? new RestBean<>(200, "查询成功", lawyers)
                : new RestBean<>(401, "已经没有数据了", null);
    }

    @Override
    public RestBean<Object> add(Lawyer lawyer) {
        int i = lawyerMapper.add(lawyer);
        return i == 0
                ? new RestBean<>(401, "添加失败", null)
                : new RestBean<>(200, "添加成功", null);
    }
}
