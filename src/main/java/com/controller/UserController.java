package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.req.UserLoginReq;
import com.req.UserQueryReq;
import com.req.UserResetPasswordReq;
import com.req.UserSaveReq;
import com.resp.PageResp;
import com.resp.UserLoginResp;
import com.resp.UserQueryResp;
import com.service.UserService;
import com.util.IMOOCJSONResult;
import com.util.SnowFlake;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;
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
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "用户列表", notes = "用户列表", httpMethod = "POST")
    @GetMapping("/list")
    public IMOOCJSONResult list(UserQueryReq req){
        PageResp<UserQueryResp> list=userService.list(req);
        return IMOOCJSONResult.ok(list);
    }
    @ApiOperation(value = "注册新用户", notes = "注册新用户", httpMethod = "POST")
    @PostMapping("/save")
    public IMOOCJSONResult save(@Valid @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.save(req);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "删除用户", notes = "删除用户", httpMethod = "POST")
    @DeleteMapping("/delete/{id}")
    public IMOOCJSONResult delete(@PathVariable Long id) {
        userService.delete(id);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "重置密码", notes = "重置密码", httpMethod = "POST")
    @PostMapping("/reset-password")
    public IMOOCJSONResult resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.resetPassword(req);
        return IMOOCJSONResult.ok();
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
    @PostMapping("/login")
    public IMOOCJSONResult login(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        UserLoginResp userLoginResp = userService.login(req);

//        if (userLoginResp != null){
            Long token = snowFlake.nextId();
            LOG.info("生成单点登录token：{}，并放入redis中", token);
            userLoginResp.setToken(token.toString());
            redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);
            return IMOOCJSONResult.ok(userLoginResp);
//        }
//
//        return IMOOCJSONResult.errorMsg("");
    }



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
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @GetMapping("/logout/{token}")
    public IMOOCJSONResult logout(@PathVariable String token) {
        redisTemplate.delete(token);
        LOG.info("从redis中删除token: {}", token);
        return IMOOCJSONResult.ok();
    }
}
