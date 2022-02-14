package com.controller;

import com.domain.Deal;
import com.service.impl.DealServiceImpl;
import com.util.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
@Api(value = "商品相关", tags = {"商品相关的api接口"})
@RestController
public class DealController {

//    private static final Logger LOG = LoggerFactory.getLogger(DealController.class);


    @Resource
    private DealServiceImpl dealService;

    // public Map<String, Object> book()
    //  public List<User> select(){
    // 查询列表的接口
    @ApiOperation(value = "商品列表", notes = "查询商品列表", httpMethod = "POST")
    @GetMapping("/prod-api/deal")
    public IMOOCJSONResult select(){
        try{
            List<Deal> list = dealService.queryDeal();
            return IMOOCJSONResult.ok(list);
        }catch (Exception e) {
            return IMOOCJSONResult.errorMsg("");
        }
    }
    // 详情接口
    @ApiOperation(value = "商品详情", notes = "商品详情", httpMethod = "POST")
    @PostMapping("/prod-api/deal_detail")
    public IMOOCJSONResult detail(@RequestBody Deal deal){
        try{
            int id = deal.getId();
            Deal result = dealService.detailDeal(id);
            return IMOOCJSONResult.ok(result);

        }catch(Exception e) {
            return IMOOCJSONResult.errorMsg("");
        }
    }
    // 修改接口
    @ApiOperation(value = "修改商品", notes = "修改商品", httpMethod = "POST")
    @PostMapping("/prod-api/deal_update")
    public IMOOCJSONResult update(@RequestBody Deal deal){
        String list = dealService.update(deal);
        return IMOOCJSONResult.ok(list);
    }
    // 新增接口
    @ApiOperation(value = "新增商品", notes = "新增商品", httpMethod = "POST")
    @PostMapping("/prod-api/deal_add")
    public IMOOCJSONResult list(@RequestBody Deal deal){
        String list = dealService.add(deal);
        return IMOOCJSONResult.ok(list);
    }
    // 删除接口
    @ApiOperation(value = "删除商品", notes = "删除商品", httpMethod = "POST")
    @PostMapping("/prod-api/deal_delete")
    public IMOOCJSONResult delete(@RequestBody Deal deal){
        int id = deal.getId();
        String list = dealService.delete(id);
        return IMOOCJSONResult.ok(list);
    }

//    @GetMapping("/deal/list")
//    public CommonResp list(DealReq req) {
//        CommonResp<List<Deal>> resp = new CommonResp<>();
//        List<Deal> list = dealService.list(req);
//        resp.setContent(list);
//        return resp;
//    }


}


