package com.clyl.cloudlaw.mapper;

import com.clyl.cloudlaw.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author xiugou798
 */
@Mapper
public interface UserMapper {

    /**
     * 检查是否有该用户
     *
     * @param user 用户
     * @return {@link User}
     */
    @Select("select *\n" +
            "from c_user where open_id = #{openId}")
    User check(User user);


    /**
     * 添加用户
     *
     * @param user 用户
     */
    @Insert("insert into c_user (user_id, user_name, open_id, password, phone, avatar, create_time, update_time)\n" +
            "values (#{userId}, #{userName}, #{openId}, #{password}, #{phone}, #{avatar}, NOW(), NOW())")
    void addUser(User user);

    @Update("update c_user\n" +
            "set user_name       = #{userName},\n" +
            "    update_time = NOW()\n" +
            "where user_id = #{userId}")
    int modName(User user);

    @Update("update c_user\n" +
            "set phone       = #{phone},\n" +
            "    update_time = NOW()\n" +
            "where user_id = #{userId}")
    int modPhone(User user);

    @Update("update c_user\n" +
            "set avatar       = #{avatar},\n" +
            "    update_time = NOW()\n" +
            "where user_id = #{userId}")
    int modAvatar(User user);

}
