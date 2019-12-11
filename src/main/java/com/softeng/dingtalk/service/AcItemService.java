package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.Application;
import com.softeng.dingtalk.repository.AcItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 12/11/2019 10:50 PM
 */
@Service
@Transactional
@Slf4j
public class AcItemService {
    @Autowired
    AcItemRepository acItemRepository;

    /**
     * @description 持久化ac申请，并将绩效申请作为外键
     * @param [acItems, application]
     * @return void
     * @date 11:28 PM 12/11/2019
     **/
    public void addAcItemList(List<AcItem> acItems, Application application) {
        for (int i = 0; i < acItems.size(); i++) {
            acItems.get(i).setApplication(application);
            acItemRepository.save(acItems.get(i));
        }
    }
}
