package com.clyl.cloudlaw.mapper;


import com.clyl.cloudlaw.entity.Lawyer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xiugou798
 */
@Mapper
public interface LawyerMapper {

    @Select("select *\n" +
            "from c_lawyers")
    List<Lawyer> getAllLawyers();

    @Select("select id,\n" +
            "       law_name,\n" +
            "       age,\n" +
            "       phone,\n" +
            "       gender,\n" +
            "       area,\n" +
            "       experience,\n" +
            "       case_results,\n" +
            "       cost,\n" +
            "       head_img,\n" +
            "       service_num,\n" +
            "       create_time\n" +
            "from c_lawyers")
    List<Lawyer> getLawyers();

    @Insert("insert into c_lawyers (law_code, law_name, age, phone, gender, area, experience, case_results, cost, head_img, service_num, create_time) " +
            "values (#{lawCode}, #{lawName}, #{age}, #{phone}, #{gender}, #{area}, #{experience}, #{caseResults}, " +
            "#{cost}, #{headImg}, #{serviceNum}, NOW())")
    int add(Lawyer lawyer);
}
