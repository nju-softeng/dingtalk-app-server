package com.softeng.dingtalk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeng.dingtalk.po.UserPo;
import com.softeng.dingtalk.po.VotePo;
import com.softeng.dingtalk.po.VoteDetailPo;
import com.softeng.dingtalk.dao.repository.VoteRepository;
import com.softeng.dingtalk.service.PaperService;
import com.softeng.dingtalk.service.VoteService;
import com.softeng.dingtalk.vo.PollVO;
import com.softeng.dingtalk.vo.VoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 5:27 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class VoteController {
    @Autowired
    VoteService voteService;
    @Autowired
    PaperService paperService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VoteRepository voteRepository;

    /**
     * 创建一个投票
     * @param voteVO
     * @return
     */
    @PostMapping("/vote")
    public VotePo addVote(@RequestBody VoteVO voteVO) {
        log.debug(voteVO.toString());
        return voteService.createVote(voteVO);
    }

    /**
     * 用户投票
     * @param vid
     * @param uid
     * @param vo
     * @return
     * @throws IOException
     */
    @PostMapping("/vote/{vid}")
    public Map addpoll(@PathVariable int vid, @RequestAttribute int uid, @RequestBody PollVO vo) throws IOException {
        VoteDetailPo voteDetailPo = new VoteDetailPo(new VotePo(vo.getVid()), vo.isResult(), new UserPo(uid));
        Map map = voteService.poll(vid, uid, voteDetailPo);
        WebSocketController.sendInfo(objectMapper.writeValueAsString(map));
        return map;
    }

    /**
     * 根据paperid获取投票详情
     * @param pid
     * @param uid
     * @return
     */
    @GetMapping("/vote/paper/{pid}/detail")
    public Map getVoteDetailByPid(@PathVariable int pid, @RequestAttribute int uid) {
        return voteService.getVotingDetails(paperService.getVoteByPid(pid).getId(), uid);
    }

    /**
     * 根据voteid获取投票详情
     * @param vid
     * @param uid
     * @return
     */
    @GetMapping("/vote/{vid}/detail")
    public Map getVoteDetailByVid(@PathVariable int vid, @RequestAttribute int uid) {
        return voteService.getVotingDetails(vid, uid);
    }

}
