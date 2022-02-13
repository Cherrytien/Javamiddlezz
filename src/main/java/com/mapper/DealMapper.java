package com.mapper;

import com.domain.Deal;
import com.domain.DealExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DealMapper {

    List queryDeal();

    Deal detailDealById(@Param("deal") Deal deal);

    int dealAdd(Deal deal);

    int update(Deal deal);

    int delete(Deal deal);

    long countByExample(DealExample example);

    int deleteByExample(DealExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Deal record);

    int insertSelective(Deal record);

    List<Deal> selectByExample(DealExample example);

    Deal selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Deal record, @Param("example") DealExample example);

    int updateByExample(@Param("record") Deal record, @Param("example") DealExample example);

    int updateByPrimaryKeySelective(Deal record);

    int updateByPrimaryKey(Deal record);
}