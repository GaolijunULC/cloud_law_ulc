package com.clyl.cloudlaw.service;
import com.clyl.cloudlaw.entity.User;
import com.clyl.cloudlaw.entity.detail.LoginDetail;
import com.clyl.cloudlaw.entity.resp.RestBean;

import java.io.IOException;

/**
 * @author xiugou798
 */
public interface UserService {

    /**
     * 登录
     *
     * @param loginDetail 登录细节
     * @return {@link RestBean}<{@link Object}>
     * @throws IOException ioexception
     */
    RestBean<Object> login(LoginDetail loginDetail) throws IOException;

    RestBean<Object> setAvatar(User ueer);

    RestBean<Object> setName(User user);

    RestBean<Object> setPhone(User user);
}
