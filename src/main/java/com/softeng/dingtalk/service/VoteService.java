package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.po.PaperinfoPO;
import com.softeng.dingtalk.repository.PaperDetailRepository;
import com.softeng.dingtalk.repository.PaperRepository;
import com.softeng.dingtalk.repository.VoteDetailRepository;
import com.softeng.dingtalk.repository.VoteRepository;
import com.softeng.dingtalk.vo.VoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 5:30 PM
 */
@Service
@Transactional
@Slf4j
public class VoteService {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    VoteDetailRepository voteDetailRepository;
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    PaperDetailRepository paperDetailRepository;
    @Autowired
    DingTalkUtils dingTalkUtils;


    // 创建投票并钉钉发送消息
    public void createVote(VoteVO voteVO) {
        Vote vote = new Vote(voteVO.getEndTime());
        voteRepository.save(vote);
        paperRepository.updatePaperVote(voteVO.getPaperid(), vote.getId());

        // 发送投票信息
        new Thread(()-> {
            String title = paperRepository.getPaperTitleById(voteVO.getPaperid());
            List<String> namelist = paperDetailRepository.listPaperAuthor(voteVO.getPaperid());
            dingTalkUtils.sendVoteMsg(voteVO.getPaperid(), title, vote.getEndTime().toString(), namelist);
        }).start();
    }

    // 用户投票
    public Map poll(int vid, int uid, VoteDetail voteDetail) {
        voteDetail.setUser(new User(uid)); // 标明这一票是谁投的
        voteDetailRepository.save(voteDetail);

        // 返回当前的投票结果
        int accept = voteDetailRepository.getAcceptCnt(vid);
        int total = voteDetailRepository.getCnt(vid);
        int reject = total - accept;
        return Map.of("status", true,"accept", accept, "total", total, "reject", reject, "result", voteDetail.getResult());
    }


    //投票还未结束,用户获取投票信息
    public Map  getVotingDetail(int vid, int uid){
        Boolean myresult = voteDetailRepository.getVoteDetail(vid, uid);
        if (myresult != null) { //用户已经投票，可以查看结果
            List<String> acceptlist =  voteDetailRepository.listAcceptNamelist(vid);
            List<String> rejectlist = voteDetailRepository.listRejectNamelist(vid);
            int accept = acceptlist.size();  // accept 票数
            int reject = rejectlist.size();  // reject 票数
            int total = accept + reject;     // 总投票数


            return Map.of("status", true,"accept", accept, "total", total, "reject", reject, "result", myresult, "acceptnames",acceptlist,"rejectnames", rejectlist);
        } else { //用户没有投票，不可以查看结果
            return Map.of("status", false);
        }
    }


    // 获取已经结束的投票信息
    public Map getVotedDetail(int vid, int uid) {
        List<String> acceptlist =  voteDetailRepository.listAcceptNamelist(vid);
        List<String> rejectlist = voteDetailRepository.listRejectNamelist(vid);
        int accept = acceptlist.size();  // accept 票数
        int reject = rejectlist.size();  // reject 票数
        int total = accept + reject;     // 总投票数
        Boolean myresult = voteDetailRepository.getVoteDetail(vid, uid); // 用户本人的投票情况：accept, reject, 未参与(null)
        return Map.of("status", true,"accept", accept, "total", total, "reject", reject, "result", myresult, "acceptnames",acceptlist,"rejectnames", rejectlist);
    }


    // 更新投票的最终结果，投票截止后调用
    public Map updateVote(int vid) {
        int accept = voteDetailRepository.getAcceptCnt(vid);
        int total = voteDetailRepository.getCnt(vid);
        voteRepository.updateStatus(vid, accept, total, accept > total - accept);
        return Map.of("accept", accept, "total", total);
    }

    // 根据论文最终结果计算投票者的ac
    public void computeVoteAc(int pid, int result) {
        Vote vote = paperRepository.findVoteById(pid);
        if (vote != null) {
            List<VoteDetail> voteDetails = voteDetailRepository.listByVid(vote.getId());
            List<AcRecord> acRecords = new ArrayList<>();
            for (VoteDetail vd : voteDetails) {
                if (vd.getResult())
            }

        }





    }


}
