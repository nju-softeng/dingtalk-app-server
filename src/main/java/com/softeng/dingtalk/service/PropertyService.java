package com.softeng.dingtalk.service;

import com.softeng.dingtalk.po.PropertyPo;
import com.softeng.dingtalk.po.UserPo;
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
    public List<PropertyPo> findByUser(int userId){
        return propertyRepository.findByUserId(userId);
    }

    /**
     * 新增固定资产
     * @param userId
     * @param propertyVO
     * @return
     */
    public PropertyPo addNewProperty(int userId, PropertyVO propertyVO){
        PropertyPo propertyPo =new PropertyPo(new UserPo(userId),propertyVO.getName(),propertyVO.getType(),propertyVO.getPreserver(),propertyVO.getRemark(),propertyVO.getStartTime());
        return propertyRepository.save(propertyPo);
    }

    /**
     * 更新固定资产
     * @param propertyVO
     */
    public void updateProperty(PropertyVO propertyVO){
        PropertyPo p = propertyRepository.findById(propertyVO.getId()).get();
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
        PropertyPo p = propertyRepository.findById(propertyId).get();
        p.setDeleted(true);
        propertyRepository.save(p);
    }
}
