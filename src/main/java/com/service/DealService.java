package com.service;
import com.domain.Deal;

import java.util.List;

/**
*
*/
public interface DealService {
    List<Deal> queryDeal();
    Deal detailDeal(int id);
    String update(Deal deal);
    String add(Deal deal);
    String delete(int id);
}
