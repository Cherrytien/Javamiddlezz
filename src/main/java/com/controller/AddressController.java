package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.domain.UserAddress;
import com.domain.bo.AddressBO;
import com.service.AddressService;
import com.util.IMOOCJSONResult;
import com.util.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "地址相关", tags = {"地址相关的api接口"})
@RequestMapping("address")
@RestController
public class AddressController {

    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作：
     * 1. 查询用户的所有收货地址列表
     * 2. 新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5. 设置默认地址
     */

    @Autowired
    private AddressService addressService;

//    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
//    @PostMapping("/list")
//    public IMOOCJSONResult list(
//           @RequestParam String userId) {
//
//        if (StringUtils.isBlank(userId)) {
//            return IMOOCJSONResult.errorMsg("");
//        }
//
//        List<UserAddress> list = addressService.queryAll(userId);
//        return IMOOCJSONResult.ok(list);
//    }

    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public IMOOCJSONResult list(
            @RequestBody String userId) {
        JSONObject jsonobject = JSONObject.parseObject(userId);
        String userId1 = jsonobject.get("userId").toString();

        if (StringUtils.isBlank(userId1)) {
            return IMOOCJSONResult.errorMsg("");
        }

        List<UserAddress> list = addressService.queryAll(userId1);
        return IMOOCJSONResult.ok(list);
    }

//    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
//    @PostMapping("/list")
//    public IMOOCJSONResult list(
//            @RequestBody AddressBO addressBO) {
//
//        String userId = addressBO.getUserId();
//
//        if (StringUtils.isBlank(userId)) {
//            return IMOOCJSONResult.errorMsg("");
//        }
//
//        List<UserAddress> list = addressService.queryAll(userId);
//        return IMOOCJSONResult.ok(list);
//    }



//    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
//    @PostMapping("/list")
//    public CommonResp list(
//            @RequestBody AddressBO addressBO) {
//
//        String userId = addressBO.getUserId();
//
//        CommonResp<List<UserAddress>> resp = new CommonResp<>();
//        List<UserAddress> list = addressService.queryAll(userId);
//        resp.setContent(list);
//        return resp;
//    }




    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestBody AddressBO addressBO) {

        IMOOCJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.addNewUserAddress(addressBO);

        return IMOOCJSONResult.ok();
    }
    private IMOOCJSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return IMOOCJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return IMOOCJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(@RequestBody AddressBO addressBO) {

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return IMOOCJSONResult.errorMsg("修改地址错误：addressId不能为空");
        }

        IMOOCJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.updateUserAddress(addressBO);

        return IMOOCJSONResult.ok();
    }

//    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
//    @PostMapping("/delete")
//    public IMOOCJSONResult delete(
//            @RequestParam String userId,
//            @RequestParam String addressId) {
//
//        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
//            return IMOOCJSONResult.errorMsg("");
//        }
//
//        addressService.deleteUserAddress(userId, addressId);
//        return IMOOCJSONResult.ok();
//    }


    @ApiOperation(value = "根据userId和addressId用户删除地址", notes = "根据userId和addressId用户删除地址", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @RequestBody String userIdAddressId
           ) {

        JSONObject jsonobject = JSONObject.parseObject(userIdAddressId);
        String userId = jsonobject.get("userId").toString();
        String addressId = jsonobject.get("addressId").toString();

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        addressService.deleteUserAddress(userId, addressId);
        return IMOOCJSONResult.ok();
    }


//    @ApiOperation(value = "根据userId和addressId用户删除地址", notes = "根据userId和addressId用户删除地址", httpMethod = "POST")
//    @PostMapping("/delete")
//    public IMOOCJSONResult delete(
//            @RequestBody AddressBO addressBO) {
//
//        String userId = addressBO.getUserId();
//        String addressId = addressBO.getAddressId();
//
//        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
//            return IMOOCJSONResult.errorMsg("");
//        }
//
//        addressService.deleteUserAddress(userId, addressId);
//        return IMOOCJSONResult.ok();
//    }

//    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
//    @PostMapping("/setDefalut")
//    public IMOOCJSONResult setDefalut(
//            @RequestParam String userId,
//            @RequestParam String addressId) {
//
//        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
//            return IMOOCJSONResult.errorMsg("");
//        }
//
//        addressService.updateUserAddressToBeDefault(userId, addressId);
//        return IMOOCJSONResult.ok();
//    }


//    @ApiOperation(value = "根据userId和addressId用户设置默认地址", notes = "根据userId和addressId用户设置默认地址", httpMethod = "POST")
//    @PostMapping("/setDefalut")
//    public IMOOCJSONResult setDefalut(
//            @RequestBody AddressBO addressBO) {
//
//        String userId = addressBO.getUserId();
//        String addressId = addressBO.getAddressId();
//
//        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
//            return IMOOCJSONResult.errorMsg("");
//        }
//
//        addressService.updateUserAddressToBeDefault(userId, addressId);
//        return IMOOCJSONResult.ok();
//    }

    @ApiOperation(value = "根据userId和addressId用户设置默认地址", notes = "根据userId和addressId用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public IMOOCJSONResult setDefalut(
            @RequestBody String userIdAddressId) {

        JSONObject jsonobject = JSONObject.parseObject(userIdAddressId);
        String userId = jsonobject.get("userId").toString();
        String addressId = jsonobject.get("addressId").toString();

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        addressService.updateUserAddressToBeDefault(userId, addressId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "根据用户id和地址id，查询具体的用户地址对象信息", notes = "根据用户id和地址id，查询具体的用户地址对象信息", httpMethod = "POST")
    @PostMapping("/oneList")
    public IMOOCJSONResult oneList(
            @RequestBody String userIdAddressId) {
        JSONObject jsonobject = JSONObject.parseObject(userIdAddressId);
        String userId = jsonobject.get("userId").toString();
        String addressId = jsonobject.get("addressId").toString();

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        UserAddress list = addressService.queryUserAddres(userId, addressId);
        return IMOOCJSONResult.ok(list);
    }


}
