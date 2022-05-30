package com.softeng.dingtalk.api;

import com.alibaba.fastjson.JSON;
import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.fabric.ChaincodeManager;
import com.softeng.dingtalk.fabric.FabricManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Component
public class BlockChainApi {
    ChaincodeManager manager=FabricManager.obtain().getManager();

    public void create(AcRecord acRecord){
        try {
            manager.invoke("create",new String[]{acRecord.getId().toString(), JSON.toJSONString(acRecord)});
            if(acRecord.getId()>getMaxId()){
                manager.invoke("update",new String[]{"max", String.valueOf(acRecord.getId())});
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
    public int getMaxId(){
        try {
            String res=manager.invoke("query",new String[]{"max"}).get("Data");
            if(res==null){
                manager.invoke("create",new String[]{"max","0"});
                return 0;
            } else {
                return Integer.parseInt(res);
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    public void update(AcRecord acRecord){
        try {
            manager.invoke("update",new String[]{acRecord.getId().toString(), JSON.toJSONString(acRecord)});
            if(acRecord.getId()>getMaxId()){
                manager.invoke("update",new String[]{"max", String.valueOf(acRecord.getId())});
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    public void delete(AcRecord acRecord){
        try {
            manager.invoke("delete",new String[]{acRecord.getId().toString()});
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    public String query(AcRecord acRecord){
        try {
            return manager.invoke("query",new String[]{acRecord.getId().toString()}).get("data");
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    public String query(String id){
        try {
            String res=manager.invoke("query",new String[]{id}).get("data");
            log.info(res);
            return res;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    public String list(List<AcRecord> acRecordList){
        try {
            String[] idArray=new String[acRecordList.size()];
            for(int i=0;i<acRecordList.size();i++){
                idArray[i]=acRecordList.get(i).getId().toString();
            }
            log.info(manager.invoke("list",idArray).get("data"));
            return manager.invoke("list",idArray).get("data");
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    public String list(String[] strings){
        try {
            String res=manager.invoke("list",strings).get("data");
            log.info(res);
            return res;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }


}
