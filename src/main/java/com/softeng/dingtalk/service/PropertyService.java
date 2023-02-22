package com.softeng.dingtalk.service;

import com.softeng.dingtalk.po_entity.Property;
import com.softeng.dingtalk.po_entity.User;
import com.softeng.dingtalk.dao.repository.PropertyRepository;
import com.softeng.dingtalk.vo.PropertyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 实验室固定资产业务逻辑
 * @Author Jerrian Zhao
 * @Data 01/26/2022
 */

@Service
@Transactional
@Slf4j
public class PropertyService {
    @Autowired
    PropertyRepository propertyRepository;

    /**
     * 查询用户所有固定资产
     * @param userId
     * @return
     */
    public List<Property> findByUser(int userId){
        return propertyRepository.findByUserId(userId);
    }

    /**
     * 新增固定资产
     * @param userId
     * @param propertyVO
     * @return
     */
    public Property addNewProperty(int userId, PropertyVO propertyVO){
        Property property =new Property(new User(userId),propertyVO.getName(),propertyVO.getType(),propertyVO.getPreserver(),propertyVO.getRemark(),propertyVO.getStartTime());
        return propertyRepository.save(property);
    }

    /**
     * 更新固定资产
     * @param propertyVO
     */
    public void updateProperty(PropertyVO propertyVO){
        Property p = propertyRepository.findById(propertyVO.getId()).get();
        p.setName(propertyVO.getName());
        p.setType(propertyVO.getType());
        p.setPreserver(propertyVO.getPreserver());
        p.setStartTime(propertyVO.getStartTime());
        p.setRemark(propertyVO.getRemark());
        propertyRepository.save(p);
    }

    /**
     * 删除固定资产
     * @param propertyId
     */
    public void deleteProperty(int propertyId){
        Property p = propertyRepository.findById(propertyId).get();
        p.setDeleted(true);
        propertyRepository.save(p);
    }
}
