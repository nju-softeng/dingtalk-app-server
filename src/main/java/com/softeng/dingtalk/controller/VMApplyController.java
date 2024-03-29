package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.entity.VMApply;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.repository.VMApplyRepository;
import com.softeng.dingtalk.service.VMApplyService;
import com.softeng.dingtalk.vo.VMApplyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class VMApplyController {
    @Autowired
    VMApplyService vmApplyService;

    /**
     * 添加或修改虚拟机申请
     * @param vmApplyVO
     * @param uid
     */
    @PostMapping("/vmApply")
    public void addVirtualMachineApply(@RequestBody VMApplyVO vmApplyVO, @RequestAttribute int uid){
        if(vmApplyVO.getId()==null){
            vmApplyService.addVMApply(vmApplyVO,uid);
        } else {
            vmApplyService.updateVMApply(vmApplyVO,uid);
        }

    }

    /**
     * 分页获取所有虚拟机申请列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/vmApply/page/{page}/{size}")
    public Map<String,Object> getVMApplyList(@PathVariable int page, @PathVariable int size){
        return vmApplyService.getVMApplyList(page,size);
    }

    /**
     * 获取处于审核的虚拟机申请列表
      * @return
     */
    @GetMapping("/vmApply/auditingList")
    public List<VMApply> getAuditingVMApplyList(){
        return vmApplyService.getAuditingVMApplyList();
    }

    /**
     * 获取某个用户虚拟机申请列表
     * @return
     */
    @GetMapping("/vmApply/user/{uid}")
    public List<VMApply> getAuditingVMApplyList(@PathVariable int uid){
        return vmApplyService.getUserVMApplyList(uid);
    }

    /**
     * 设置虚拟机申请结果
     * @param id
     * @param isPass
     * @param uid
     */
    @PatchMapping("/vmApply/{id}/{isPass}")
    public void setVMApplyResult(@PathVariable int id,@PathVariable boolean isPass,@RequestAttribute int uid){
        vmApplyService.setVMApplyState(id,isPass,uid);
    }

    /**
     * 删除虚拟机申请
     * @param id
     * @param uid
     */
    @DeleteMapping("/vmApply/{id}")
    public void deleteVMApply(@PathVariable int id,@RequestAttribute int uid){
        vmApplyService.deleteVMApply(id,uid);
    }
}
