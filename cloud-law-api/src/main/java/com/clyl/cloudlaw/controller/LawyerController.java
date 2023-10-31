package com.clyl.cloudlaw.controller;


import com.clyl.cloudlaw.entity.Lawyer;
import com.clyl.cloudlaw.entity.Talk;
import com.clyl.cloudlaw.entity.resp.RestBean;
import com.clyl.cloudlaw.service.LawyerService;
import com.clyl.cloudlaw.service.TalkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiugou798
 */
@RestController
@RequestMapping("/api/lawyer")
public class LawyerController {
    @Resource
    private LawyerService lawyerService;

    @GetMapping("/getAllLawyers")
    public RestBean<List<Lawyer>> getAllLawyers() {
        return lawyerService.getAllLawyers();
    }

    @GetMapping("/getLawyers")
    public RestBean<List<Lawyer>> getLawyer() {
        return lawyerService.getLawyers();
    }

    @PostMapping("/add")
    public RestBean<Object> add(@RequestBody Lawyer lawyer) {
        return lawyerService.add(lawyer);
    }
}
