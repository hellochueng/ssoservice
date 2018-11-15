package com.sso.service.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.projectshop.pojo.MoviceType;
import org.projectshop.pojo.MoviceTypeExample;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MoviceTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    int countByExample(MoviceTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    int deleteByExample(MoviceTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    int insert(MoviceType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    int insertSelective(MoviceType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    List<MoviceType> selectByExample(MoviceTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    MoviceType selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") MoviceType record, @Param("example") MoviceTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") MoviceType record, @Param("example") MoviceTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(MoviceType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table movice_type
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(MoviceType record);
}