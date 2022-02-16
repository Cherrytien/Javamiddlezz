package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.domain.User;
import com.req.UserLoginReq;
import com.req.UserQueryReq;
import com.req.UserResetPasswordReq;
import com.req.UserSaveReq;
import com.resp.PageResp;
import com.resp.UserLoginResp;
import com.resp.UserQueryResp;
import com.service.UserService;
import com.util.IMOOCJSONResult;
import com.util.RedisOperator;
import com.util.SnowFlake;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(value = "用户相关", tags = {"用户相关的api接口"})
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户列表", notes = "用户列表", httpMethod = "GET")
    @GetMapping("/list")
    public IMOOCJSONResult list(UserQueryReq req){
        PageResp<UserQueryResp> list=userService.list(req);
        return IMOOCJSONResult.ok(list);
    }
    @ApiOperation(value = "注册新用户", notes = "注册新用户", httpMethod = "POST")
    @PostMapping("/save")
    public IMOOCJSONResult save(@RequestBody UserSaveReq req) {
        String username = req.getLoginName();
        String password = req.getPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 查询用户名是否存在
        User userDB = userService.selectByLoginName(username);
        if (!ObjectUtils.isEmpty(userDB)) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }

        // 2. 密码长度不能少于6位
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能少于6");
        }

        // 3. 判断两次密码是否一致
//        if (!password.equals(confirmPwd)) {
//            return IMOOCJSONResult.errorMsg("两次密码输入不一致");
//        }

        // 4. 实现注册
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.save(req);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "删除用户", notes = "删除用户", httpMethod = "DELETE")
    @DeleteMapping("/delete/{id}")
    public IMOOCJSONResult delete(@PathVariable Long id) {
        userService.delete(id);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "重置密码", notes = "重置密码", httpMethod = "POST")
    @PostMapping("/reset-password")
    public IMOOCJSONResult resetPassword(@RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.resetPassword(req);
        return IMOOCJSONResult.ok();
    }


    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserLoginReq req) {
        String username = req.getLoginName();
        String password = req.getPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 实现登录
        req.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        UserLoginResp userLoginResp = userService.login(req);

        if (userLoginResp == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }
        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token：{}，并放入redis中", token);
        userLoginResp.setToken(token.toString());
        redisOperator.set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24);
        return IMOOCJSONResult.ok(userLoginResp);

    }



//    @PostMapping("/login")
//    public CommonResp login(@Valid @RequestBody UserLoginReq req) {
//        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
//        CommonResp<UserLoginResp> resp = new CommonResp<>();
//        UserLoginResp userLoginResp = userService.login(req);
//
//        Long token = snowFlake.nextId();
//        LOG.info("生成单点登录token：{}，并放入redis中", token);
//        userLoginResp.setToken(token.toString());
//        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);
////        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);
//
//        resp.setContent(userLoginResp);
//        return resp;
//    }
//    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
//    @PostMapping("/login")
//    public IMOOCJSONResult login(@Valid @RequestBody UserLoginReq req) {
//        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
//        UserLoginResp userLoginResp = userService.login(req);


//    @PostMapping("/login")
//    public IMOOCJSONResult login(@Valid @RequestBody UserLoginReq req) {
//        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
//        UserLoginResp userLoginResp = userService.login(req);

//        if (userLoginResp != null){
//            Long token = snowFlake.nextId();
//            LOG.info("生成单点登录token：{}，并放入redis中", token);
//            userLoginResp.setToken(token.toString());
//            redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);
//            return IMOOCJSONResult.ok(userLoginResp);
//        }
//
//        return IMOOCJSONResult.errorMsg("");
//    }



//    @PostMapping("/login")
//    public CommonResp<UserLoginResp> login(@Valid @RequestBody UserLoginReq req) {
//        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
//        CommonResp<UserLoginResp> resp = userService.login(req);
//
//        if (resp.getSuccess()){
//            Long token = snowFlake.nextId();
//            LOG.info("生成单点登录token：{}，并放入redis中", token);
//            UserLoginResp uL = resp.getContent();
//            uL.setToken(token.toString());
//            resp.setContent(uL);
//            redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(resp.getContent()), 3600 * 24, TimeUnit.SECONDS);
//            return resp;
//        }
//
//        return resp;
//    }
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "GET")
    @GetMapping("/logout/{token}")
    public IMOOCJSONResult logout(@PathVariable String token) {
        redisOperator.del(token);
        LOG.info("从redis中删除token: {}", token);
        return IMOOCJSONResult.ok();
    }
}
