package com.service;

import com.domain.MerchantUser;
import com.domain.MerchantUserExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.MerchantUserMapper;
import com.req.MerchantUserLoginReq;
import com.req.MerchantUserQueryReq;
import com.req.MerchantUserResetPasswordReq;
import com.req.MerchantUserSaveReq;
import com.resp.PageResp;
import com.resp.MerchantUserLoginResp;
import com.resp.MerchantUserQueryResp;
import com.util.CopyUtil;
import com.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MerchantUserService {


    private static final Logger LOG = LoggerFactory.getLogger(MerchantUserService.class);

    @Resource
    private MerchantUserMapper merchantuserMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<MerchantUserQueryResp> list(MerchantUserQueryReq req){

        MerchantUserExample merchantuserExample = new MerchantUserExample();
        MerchantUserExample.Criteria criteria = merchantuserExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andNameLike("%" + req.getLoginName() + "%");
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<MerchantUser> merchantuserslist = merchantuserMapper.selectByExample(merchantuserExample);

        PageInfo<MerchantUser> pageInfo = new PageInfo<>(merchantuserslist);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


        List<MerchantUserQueryResp> respList = new ArrayList<>();
//        for (MerchantUser merchantuser : merchantuserslist) {
////            MerchantUserRsp merchantuserRsp = new MerchantUserRsp();
////            BeanUtils.copyProperties(merchantuser,merchantuserRsp);
//            //对象复制
//            MerchantUserRsp merchantuserRsp = CopyUtil.copy(merchantuser, MerchantUserRsp.class);
//
//            respList.add(merchantuserRsp);
//        }



        //列表复制
        List<MerchantUserQueryResp> list = CopyUtil.copyList(merchantuserslist, MerchantUserQueryResp.class);

        PageResp<MerchantUserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

//    public void save(MerchantUserSaveReq req) {
//        MerchantUser merchantuser = CopyUtil.copy(req, MerchantUser.class);
//        if (ObjectUtils.isEmpty(req.getId())) {
//            // 新增
//            merchantuser.setId(snowFlake.nextId());
//            merchantuserMapper.insert(merchantuser);
//        } else {
//            // 更新
//            merchantuserMapper.updateByPrimaryKey(merchantuser);
//        }
//    }

    public void save(MerchantUserSaveReq req) {
        MerchantUser merchantuser = CopyUtil.copy(req, MerchantUser.class);
        if (ObjectUtils.isEmpty(req.getId())) {
//            MerchantUser merchantuserDB = selectByLoginName(req.getLoginName());
//            if (ObjectUtils.isEmpty(merchantuserDB)) {
                // 新增
                merchantuser.setId(snowFlake.nextId());
                merchantuserMapper.insert(merchantuser);
//            } else {
//                // 用户名已存在
//                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
//            }
        } else {
            // 更新
            merchantuser.setLoginName(null);
            merchantuser.setPassword(null);
            merchantuserMapper.updateByPrimaryKeySelective(merchantuser);
        }
    }




    public void delete(Long id) {
        merchantuserMapper.deleteByPrimaryKey(id);
    }


    public MerchantUser selectByLoginName(String LoginName) {
        MerchantUserExample merchantuserExample = new MerchantUserExample();
        MerchantUserExample.Criteria criteria = merchantuserExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<MerchantUser> merchantuserList = merchantuserMapper.selectByExample(merchantuserExample);
        if (CollectionUtils.isEmpty(merchantuserList)) {
            return null;
        } else {
            return merchantuserList.get(0);
        }
    }

    /**
     * 修改密码
     */
    public void resetPassword(MerchantUserResetPasswordReq req) {
        MerchantUser merchantuser = CopyUtil.copy(req, MerchantUser.class);
        merchantuserMapper.updateByPrimaryKeySelective(merchantuser);
    }


    /**
     * 登录
     */
    public MerchantUserLoginResp login(MerchantUserLoginReq req) {
        MerchantUser merchantuserDb = selectByLoginName(req.getLoginName());
//        if (ObjectUtils.isEmpty(merchantuserDb)) {
//            // 用户名不存在
//            LOG.info("用户名不存在, {}", req.getLoginName());
//            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
//
//        } else {
            if (merchantuserDb.getPassword().equals(req.getPassword())) {
                // 登录成功
                return CopyUtil.copy(merchantuserDb, MerchantUserLoginResp.class);
            } else {
//                // 密码不对
//                LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", req.getPassword(), merchantuserDb.getPassword());
//                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
                return null;
//            }
        }
    }


//    public CommonResp<MerchantUserLoginResp> login(MerchantUserLoginReq req) {
//        MerchantUser merchantuserDb = selectByLoginName(req.getLoginName());
//        if (ObjectUtils.isEmpty(merchantuserDb)) {
//            // 用户名不存在
//            LOG.info("用户名不存在, {}", req.getLoginName());
//            CommonResp<MerchantUserLoginResp> Cr = new CommonResp<>();
//            Cr.setSuccess(false);
//            Cr.setMessage("用户名不存在");
//            return Cr;
//
//        } else {
//            if (merchantuserDb.getPassword().equals(req.getPassword())) {
//                // 登录成功
//                MerchantUserLoginResp merchantuserLoginResp = CopyUtil.copy(merchantuserDb, MerchantUserLoginResp.class);
//                CommonResp<MerchantUserLoginResp> Cr = new CommonResp<>();
//                Cr.setContent(merchantuserLoginResp);
//                return Cr;
//            } else {
//                // 密码不对
//                LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", req.getPassword(), merchantuserDb.getPassword());
//                CommonResp<MerchantUserLoginResp> Cr = new CommonResp<>();
//                Cr.setSuccess(false);
//                Cr.setMessage("密码不对");
//                return Cr;
//            }
//        }
//    }

}
