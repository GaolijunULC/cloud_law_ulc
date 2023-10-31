package com.clyl.cloudlaw.service;

import com.clyl.cloudlaw.entity.Msg;
import com.clyl.cloudlaw.entity.Talk;
import com.clyl.cloudlaw.entity.resp.RestBean;

import java.io.IOException;
import java.util.List;

/**
 * @author xiugou798
 */
public interface TalkService {

    /**
     * 得到讨论列表
     * 登录
     *
     * @param userId 用户id
     * @return {@link RestBean}<{@link List}<{@link Talk}>>
     */
    RestBean<List<Talk>> getTalkList(String userId);

    RestBean<Talk> newTalk(Talk talk);

    RestBean<List<Msg>> getMsg(String  talkId);

    RestBean<Msg> sendMsg(Msg msg) throws IOException;

    RestBean<Talk> delTalk(Talk talk);
}
