package com.service.impl;

import com.domain.Deal;
import com.domain.DealExample;
import com.mapper.DealMapper;
import com.req.DealReq;
import com.service.DealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DealServiceImpl implements DealService {

    @Resource
    private DealMapper dealMapper;

    @Override
    public List queryDeal(){
        //return  dealMapper.queryDeal();
        return (List) dealMapper.queryDeal();
    }
    @Override
    public Deal detailDeal(int id) {
        Deal deal = new Deal();
        deal.setId(id);
        return dealMapper.detailDealById(deal);
    }
    @Override
    public String add(Deal deal) {
        int num = dealMapper.dealAdd(deal);
        if (num>0){
            return "新增成功！";
        }
        return "新增失败！";
    }
    @Override
    public String update(Deal deal){
        int num = dealMapper.update(deal);
        if (num > 0){
            return "修改成功";
        }
        return "修改失败";
    }
    @Override
    public String delete(int id){
        Deal deal = new Deal();
        deal.setId(id);
        int num = dealMapper.delete(deal);
        if (num > 0){
            return "删除成功";
        }
        return "删除失败";
    }

    public List<Deal> list(DealReq req) {

        DealExample dealExample = new DealExample();
        DealExample.Criteria criteria = dealExample.createCriteria();
        if(req.getName() == null){
            req.setName("");
        }
        criteria.andNameLike("%" + req.getName() + "%");
        return dealMapper.selectByExample(dealExample);

    }
}
