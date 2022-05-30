package com.softeng.dingtalk.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.softeng.dingtalk.api.BlockChainApi;
import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.vo.ConflictVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class BlockChainService {
    @Autowired
    BlockChainApi blockChainApi;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    UserRepository userRepository;
    public List<ConflictVO> checkACRecord(){
        List<ConflictVO> conflictVOList=new LinkedList<>();
        int max= Math.max(blockChainApi.getMaxId(),acRecordRepository.getMaxId());
        String[] ids=new String[max];
        for(int i=0;i<ids.length;i++){
            ids[i]=String.valueOf(i+1);
        }
        List<String> blockDataList=getBlockChainResList(blockChainApi.list(ids));
        for(int i=0;i<blockDataList.size();i++){
            AcRecord acRecord=acRecordRepository.findById(Integer.parseInt(ids[i])).orElse(null);
            AcRecord blockData = null;
            if(blockDataList.get(i).length()>0){
                blockData=JSONObject.parseObject(blockDataList.get(i),AcRecord.class);
            }
            if(!isSame(acRecord,blockData)){
                conflictVOList.add(new ConflictVO(acRecord,blockData,null));
            }
        }
        return conflictVOList.subList(0,6);
    }

    private boolean isSame(AcRecord a,AcRecord b){
        if(a==null && b==null) return true;
        if(a==null || b==null) return false;
        if(a.getId().equals(b.getId()) && a.getAc()==b.getAc() && a.getReason().equals(b.getReason()) &&
        a.getClassify()==b.getClassify() && a.getCreateTime().equals(b.getCreateTime()) && isUserSame(a.getUser(),b.getUser()) &&
        isUserSame(a.getAuditor(),b.getAuditor())) return true;
        return false;
    }

    private boolean isUserSame(User a, User b){
        if(a==null && b==null) return true;
        if((a==null && b.getId()==null) || (b==null && a.getId()==null)) {
            return true;
        }
        if((a!=null && b!=null) && a.getId().equals(b.getId())) return true;
        return false;
    }

    private List<String> getBlockChainResList(String res){
        if(res.charAt(0)=='[' && res.charAt(1)==',') res=res.substring(0,1)+"\"\""+res.substring(1);
        if(res.charAt(res.length()-1)==']' && res.charAt(res.length()-2)==',') res=res.substring(0,res.length()-1)+"\"\""+res.substring(res.length()-1);
        for(int i=0;i<res.length()-1;i++){
            if(res.charAt(i)==',' && res.charAt(i+1)==',') res=res.substring(0,i+1)+"\"\""+res.substring(i+1);
        }
        log.info(res);
        return JSONArray.parseArray(res,String.class);
    }

    public void decideConflict(ConflictVO conflictVO){
        if(conflictVO.getChoice().equals("fabric")){
            if(conflictVO.getFabricData().getId()==null){
                //如果区块链数据为空，那么Mysql一定不为空
                acRecordRepository.delete(conflictVO.getMysqlData());
            } else {
                AcRecord acRecord=conflictVO.getFabricData();
                if(conflictVO.getMysqlData().getId()==null){
                    blockChainApi.delete(acRecord);
                    acRecord.setId(null);
                }
                acRecord.setUser(userRepository.findById(acRecord.getUser().getId()).orElse(null));
                if(acRecord.getAuditor()!=null && acRecord.getAuditor().getId()==null) acRecord.setAuditor(null);
                //acRecord.setAuditor(userRepository.findById(acRecord.getAuditor().getId()).orElse(null));
                acRecordRepository.save(conflictVO.getFabricData());
            }
        } else {
            if(conflictVO.getMysqlData().getId()==null){
                blockChainApi.delete(conflictVO.getFabricData());
            } else {
                if(conflictVO.getFabricData().getId()==null){
                    blockChainApi.create(conflictVO.getMysqlData());
                } else {
                    blockChainApi.update(conflictVO.getMysqlData());
                }
            }
        }
    }
}
