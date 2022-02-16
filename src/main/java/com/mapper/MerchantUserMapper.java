package com.mapper;

import com.domain.MerchantUser;
import com.domain.MerchantUserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantUserMapper {
    long countByExample(MerchantUserExample example);

    int deleteByExample(MerchantUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MerchantUser record);

    int insertSelective(MerchantUser record);

    List<MerchantUser> selectByExample(MerchantUserExample example);

    MerchantUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MerchantUser record, @Param("example") MerchantUserExample example);

    int updateByExample(@Param("record") MerchantUser record, @Param("example") MerchantUserExample example);

    int updateByPrimaryKeySelective(MerchantUser record);

    int updateByPrimaryKey(MerchantUser record);
}