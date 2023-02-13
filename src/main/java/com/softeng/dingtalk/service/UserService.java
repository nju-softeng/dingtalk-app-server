package com.softeng.dingtalk.service;

import com.softeng.dingtalk.api.BaseApi;
import com.softeng.dingtalk.component.convertor.PermissionConvertor;
import com.softeng.dingtalk.component.convertor.TeamConvertor;
import com.softeng.dingtalk.dao.repository.*;
import com.softeng.dingtalk.encryption.Encryption;
import com.softeng.dingtalk.entity.Permission;
import com.softeng.dingtalk.entity.Team;
import com.softeng.dingtalk.po.*;
import com.softeng.dingtalk.utils.StreamUtils;
import com.softeng.dingtalk.vo.UserInfoVO;
import com.softeng.dingtalk.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zhanyeye
 * @description User业务逻辑组件
 * @date 12/7/2019
 */
@Service
@Transactional
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AcRecordRepository acRecordRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    BaseApi baseApi;
    @Autowired
    Encryption encryption;
    @Resource
    TeamConvertor teamConvertor;
    @Resource
    PermissionConvertor permissionConvertor;

    /**
     * @author LiXiaoKang
     * @description 新增用用户权限、用户组相关注入
     * @date 2/5/2023
     */
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserPermissionRepository userPermissionRepository;
    @Autowired
    private UserTeamRepository userTeamRepository;

    @Value("${file.userLeaseContractFilePath}")
    private String userLeaseContractFilePath;

//    /**
//     * 判断用户权限是否为审核人
//     * @param uid 用户id
//     * @return
//     */
//    public boolean isAuditor(int uid) {
//        return userRepository.getUserAuthority(uid) == UserPo.AUDITOR_AUTHORITY;
//    }


    /**
     * 查询系统可用用户
     * @return
     */
    public List<UserVO> listUserVO() {
        return userRepository.listUserVOS();
    }


    /**
     * 获取用户的userID （获取周报）
     * @param id
     * @return
     */
    public String getUserid(int id) {
        return userRepository.findById(id).get().getUserid();
    }

    public String getUserUnionId(int uid) {return userRepository.findById(uid).get().getUnionid();}
    /**
     * 通过useID获取用户 -> 通过用户进入系统时调用API获得的userID查询用户，判断用户是否在系统中，还是新用户
     * @param userid
     * @return
     */
    public UserPo getUser(String userid) {
        return userRepository.findByUserid(userid);
    }


    /**
     * 查询所有的审核员 -> 供用户提交审核申请时选择
     * @return
     */
    public Map getAuditorUser() {
        return Map.of("auditorlist", userRepository.listAuditor());
    }


    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    public Map getUserInfo(int uid) {
        UserPo u = userRepository.findById(uid).get();
        double ac = acRecordRepository.getUserAcSum(uid);
        baseApi.getJsapiTicket(); // 提前拿到jsapi ticket，避免需要时再去那减少时延
        if (u.getAvatar() != null) {
            return Map.of("name", u.getName(), "avatar", u.getAvatar(), "ac", ac, "userid", u.getUserid());
        } else {
            return Map.of("name", u.getName(), "ac", ac, "userid", u.getUserid());
        }
    }


//    /**
//     * 更新用户权限
//     * @param uid
//     * @param authority
//     */
//    public void updateRole(int uid, int authority) {
//        userRepository.updateUserRole(uid, authority);
//    }


    /**
     * 查询用户详情
     * @param uid
     * @return
     */
    public UserInfoVO getUserDetail(int uid) {
        UserPo u = userRepository.findById(uid).get();
        return new UserInfoVO(u.getName(), u.getAvatar(), u.getPosition(), u.getStuNum(), u.getUndergraduateCollege(), u.getMasterCollege(), encryption.doDecrypt(u.getIdCardNo()), encryption.doDecrypt(u.getCreditCard()), u.getBankName(),u.getRentingStart(), u.getRentingEnd(), u.getAddress(), u.getWorkState(), u.getRemark(),u.getLeaseContractFileName(),u.getLeaseContractFilePath());
    }


    public void updateUserInfo(UserInfoVO userInfoVO, int uid) {
        UserPo u = userRepository.findById(uid).get();
        u.setStuNum(userInfoVO.getStuNum());
        u.setName(userInfoVO.getName());
        u.setCreditCard(encryption.doEncrypt(userInfoVO.getCreditCard()));
        u.setIdCardNo(encryption.doEncrypt(userInfoVO.getIdCardNo()));
        u.setBankName(userInfoVO.getBankName());
        u.setMasterCollege(userInfoVO.getMasterCollege());
        u.setUndergraduateCollege(userInfoVO.getUndergraduateCollege());
        u.setWorkState(userInfoVO.getWorkState());
        u.setRemark(userInfoVO.getRemark());
        u.setAddress(userInfoVO.getAddress());
        u.setRentingEnd(userInfoVO.getRentingEnd());
        u.setRentingStart(userInfoVO.getRentingStart());
        userRepository.save(u);
    }

    public void saveLeaseContractFile(MultipartFile file, int uid){
       UserPo userPo =userRepository.findById(uid).get();
       userPo.setLeaseContractFileName(file.getOriginalFilename());
       userPo.setLeaseContractFilePath(fileService.addFileByPath(file,userLeaseContractFilePath+ userPo.getStuNum()));
       userRepository.save(userPo);
    }

    public void downloadContractFile(int uid, HttpServletResponse httpServletResponse) throws IOException {
        UserPo userPo =userRepository.findById(uid).get();
        String fileName= userPo.getLeaseContractFileName();
        String filePath= userPo.getLeaseContractFilePath();
        fileService.downloadFile(fileName,filePath,httpServletResponse);
    }

    /**
     * @author LiXiaoKang
     * @description 新增用用户权限、用户组相关service层方法
     * @date 2/5/2023
     */

    /**
     * 获得该用户的所有权限名
     * @param userId
     * @return 所有权限名
     */
    public List<Permission> getPermissions(int userId){
        List<UserPermissionPo> userPermissionPoList = userPermissionRepository.findAllByUserId(userId);
        return StreamUtils.map(
                userPermissionPoList,
                userPermissionPo -> permissionConvertor.po2Entity(permissionRepository.findById(userPermissionPo.getPermissionId()))
        );
    }

    /**
     * 获得该用户所在的所有用户组名
     * @param userId
     * @return
     */
    public List<Team> getTeams(int userId){
        List<UserTeamPo> userTeamPoList = userTeamRepository.findAllByUserId(userId);
        return StreamUtils.map(
                userTeamPoList,
                userTeamPo -> teamConvertor.po2Entity(teamRepository.findById(userTeamPo.getTeamId()))
        );
    }
}
