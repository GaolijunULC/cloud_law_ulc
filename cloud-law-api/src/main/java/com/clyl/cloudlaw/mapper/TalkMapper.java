package com.clyl.cloudlaw.mapper;

import com.clyl.cloudlaw.entity.Msg;
import com.clyl.cloudlaw.entity.Talk;
import com.clyl.cloudlaw.entity.User;
import com.clyl.cloudlaw.entity.resp.RestBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author xiugou798
 */
@Mapper
public interface TalkMapper {

    @Select("select *\n" +
            "from c_talks\n" +
            "where user_id = #{userId} and is_delete = '0' order by update_time desc")
    List<Talk> getTalkList(String userId);

    @Insert("insert into c_talks (talk_id, user_id, title, talk_count, create_time, update_time, is_delete)\n" +
            "values (#{talkId}, #{userId}, #{title}, 0, NOW(), NOW(), '0')")
    int newTalk(Talk talk);

    @Select("select *\n" +
            "from c_msg\n" +
            "where talk_id = #{talkId} order by create_time")
    List<Msg> getMsgs(String talkId);

    @Insert("insert into c_msg (talk_id, initiator, content, create_time)\n" +
            "values (#{talkId}, '0', #{content}, NOW())")
    int addPeopleMsg(Msg msg);

    @Insert("insert into c_msg (talk_id, initiator, content, create_time)\n" +
            "values (#{talkId}, '1', #{content}, NOW())")
    int addChatMsg(Msg msg);

    @Update("update c_talks\n" +
            "set talk_count = talk_count + 1 ,\n" +
            "update_time = NOW()\n" +
            "where talk_id = #{talkId}")
    int addCount(Msg msg);

    @Update("update c_talks\n" +
            "set is_delete = '1'\n" +
            "where talk_id = #{talkId}")
    int delTalk(Talk talk);
}
