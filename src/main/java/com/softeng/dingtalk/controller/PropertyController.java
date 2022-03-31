package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Property;
import com.softeng.dingtalk.service.PropertyService;
import com.softeng.dingtalk.vo.PropertyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 固定资产
 * @Author Jerrian Zhao
 * @Data 01/25/2022
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class PropertyController {
    @Autowired
    PropertyService propertyService;

    /**
     * 获取用户的固定资产
     *
     * @param uid
     * @return
     */
    @GetMapping("user/{uid}/propertyList")
    public List<Property> findByUserId(@PathVariable int uid) {
        return propertyService.findByUser(uid);
    }

    /**
     * 新增固定资产
     * @param uid
     * @param propertyVO
     */
    @PutMapping("user/{uid}/property")
    public void addNewProperty(@PathVariable int uid, @RequestBody PropertyVO propertyVO){
        propertyService.addNewProperty(uid,propertyVO);
    }

    /**
     * 更新资产信息
     * @param propertyVO
     */
    @PostMapping("/property/update")
    public void updateProperty(@RequestBody PropertyVO propertyVO){
        propertyService.updateProperty(propertyVO);
    }

    /**
     * 删除固定资产
     * @param propertyId
     */
    @DeleteMapping("/property")
    public void deleteProperty(@RequestBody int propertyId){
        propertyService.deleteProperty(propertyId);
    }
}
