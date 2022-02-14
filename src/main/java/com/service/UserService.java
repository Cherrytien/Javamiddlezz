package com.service;

import com.domain.User;
import com.domain.UserExample;
import com.exception.BusinessException;
import com.exception.BusinessExceptionCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.UserMapper;
import com.req.UserLoginReq;
import com.req.UserQueryReq;
import com.req.UserResetPasswordReq;
import com.req.UserSaveReq;
import com.resp.PageResp;
import com.resp.UserLoginResp;
import com.resp.UserQueryResp;
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
public class UserService {


    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req){

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andNameLike("%" + req.getLoginName() + "%");
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<User> userslist = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userslist);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


        List<UserQueryResp> respList = new ArrayList<>();
//        for (User user : userslist) {
////            UserRsp userRsp = new UserRsp();
////            BeanUtils.copyProperties(user,userRsp);
//            //对象复制
//            UserRsp userRsp = CopyUtil.copy(user, UserRsp.class);
//
//            respList.add(userRsp);
//        }



        //列表复制
        List<UserQueryResp> list = CopyUtil.copyList(userslist, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

//    public void save(UserSaveReq req) {
//        User user = CopyUtil.copy(req, User.class);
//        if (ObjectUtils.isEmpty(req.getId())) {
//            // 新增
//            user.setId(snowFlake.nextId());
//            userMapper.insert(user);
//        } else {
//            // 更新
//            userMapper.updateByPrimaryKey(user);
//        }
//    }

    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
//            User userDB = selectByLoginName(req.getLoginName());
//            if (ObjectUtils.isEmpty(userDB)) {
                // 新增
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
//            } else {
//                // 用户名已存在
//                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
//            }
        } else {
            // 更新
            user.setLoginName(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }




    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }


    public User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    /**
     * 修改密码
     */
    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }


    /**
     * 登录
     */
    public UserLoginResp login(UserLoginReq req) {
        User userDb = selectByLoginName(req.getLoginName());
//        if (ObjectUtils.isEmpty(userDb)) {
//            // 用户名不存在
//            LOG.info("用户名不存在, {}", req.getLoginName());
//            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
//
//        } else {
            if (userDb.getPassword().equals(req.getPassword())) {
                // 登录成功
                return CopyUtil.copy(userDb, UserLoginResp.class);
            } else {
//                // 密码不对
//                LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", req.getPassword(), userDb.getPassword());
//                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
                return null;
//            }
        }
    }


//    public CommonResp<UserLoginResp> login(UserLoginReq req) {
//        User userDb = selectByLoginName(req.getLoginName());
//        if (ObjectUtils.isEmpty(userDb)) {
//            // 用户名不存在
//            LOG.info("用户名不存在, {}", req.getLoginName());
//            CommonResp<UserLoginResp> Cr = new CommonResp<>();
//            Cr.setSuccess(false);
//            Cr.setMessage("用户名不存在");
//            return Cr;
//
//        } else {
//            if (userDb.getPassword().equals(req.getPassword())) {
//                // 登录成功
//                UserLoginResp userLoginResp = CopyUtil.copy(userDb, UserLoginResp.class);
//                CommonResp<UserLoginResp> Cr = new CommonResp<>();
//                Cr.setContent(userLoginResp);
//                return Cr;
//            } else {
//                // 密码不对
//                LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", req.getPassword(), userDb.getPassword());
//                CommonResp<UserLoginResp> Cr = new CommonResp<>();
//                Cr.setSuccess(false);
//                Cr.setMessage("密码不对");
//                return Cr;
//            }
//        }
//    }

}
