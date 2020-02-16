package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.entity.VoteDetail;
import com.softeng.dingtalk.po.Paperinfo1PO;
import com.softeng.dingtalk.repository.PaperDetailRepository;
import com.softeng.dingtalk.repository.PaperRepository;
import com.softeng.dingtalk.repository.VoteDetailRepository;
import com.softeng.dingtalk.repository.VoteRepository;
import com.softeng.dingtalk.vo.VoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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


    public void createVote(VoteVO voteVO) {
        Vote vote = new Vote(voteVO.getEndTime());
        voteRepository.save(vote);
        paperRepository.updatePaperVote(voteVO.getPaperid(), vote.getId());

        // 发送投票信息
        new Thread(()-> {
            Paperinfo1PO paperinfo1PO = paperRepository.getPaperInfo1(voteVO.getPaperid());
            List<String> namelist = paperDetailRepository.listPaperAuthor(voteVO.getPaperid());
            dingTalkUtils.sendVoteMsg(voteVO.getPaperid(), paperinfo1PO.getTitle(), paperinfo1PO.getEndTime().toString(), namelist);
        }).start();
    }

    public void poll(VoteDetail voteDetail) {
        voteDetailRepository.save(voteDetail);
    }




    //投票结束前,投过票的人获取投票信息
    public Map  getVoteDetail(int vid, int uid){
        if (voteDetailRepository.isExist(vid, uid) != 0) {
            int accept = voteDetailRepository.getAcceptCnt(vid);
            int total = voteDetailRepository.getCnt(vid);
            int reject = total - accept;
            return Map.of("status", true,"accept", accept, "total", total, "reject", reject);
        } else {
            return Map.of("status", false);
        }
    }


    public Map getVoteDetail(int vid) {
        int accept = voteDetailRepository.getAcceptCnt(vid);
        int total = voteDetailRepository.getCnt(vid);
        int reject = total - accept;
        return Map.of("status", true,"accept", accept, "total", total, "reject", reject);
    }


    //投票截止后更新结果，
    public Map updateVote(int vid) {
        int accept = voteDetailRepository.getAcceptCnt(vid);
        int total = voteDetailRepository.getCnt(vid);
        voteRepository.updateStatus(vid, accept, total, accept > total - accept);
        return Map.of("accept", accept, "total", total);
    }


}
