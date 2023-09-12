package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.aspect.AccessPermission;
import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.req.NewsReq;
import com.softeng.dingtalk.dto.resp.NewsResp;
import com.softeng.dingtalk.enums.PermissionEnum;
import com.softeng.dingtalk.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v2/news")
public class NewsController {

    @Resource
    private NewsService newsService;

    @GetMapping("/shownNews")
    public CommonResult<List<NewsResp>> getShownNews() {
        List<NewsResp> data = newsService.getShownNews();
        return CommonResult.success(data);
    }

    @GetMapping("/AllNews/{page}/{size}")
    @AccessPermission(PermissionEnum.EDIT_SCROLL_NEWS_BOARD)
    public CommonResult<Map<String, Object>> getAllNews(@PathVariable int page, @PathVariable int size) {
        Map<String, Object>  data = newsService.getAllNews(page, size);
        return CommonResult.success(data);
    }

    @GetMapping("/shownNews/{page}/{size}")
    @AccessPermission(PermissionEnum.EDIT_SCROLL_NEWS_BOARD)
    public CommonResult<Map<String, Object> > getShownNews(@PathVariable int page, @PathVariable int size) {
        Map<String, Object>  data = newsService.getShownNews(page, size);
        return CommonResult.success(data);
    }

    @GetMapping("/notShownNews/{page}/{size}")
    @AccessPermission(PermissionEnum.EDIT_SCROLL_NEWS_BOARD)
    public CommonResult<Map<String, Object> > getNotShownNews(@PathVariable int page, @PathVariable int size) {
        Map<String, Object> data = newsService.getNotShownNews(page, size);
        return CommonResult.success(data);
    }

    @PostMapping("")
    @AccessPermission(PermissionEnum.EDIT_SCROLL_NEWS_BOARD)
    public CommonResult<String> addNews(@RequestBody NewsReq newsReq) {
        newsService.addNews(newsReq);
        return CommonResult.success("添加成功");
    }

    @PutMapping ("/shownNews/{newsId}")
    @AccessPermission(PermissionEnum.EDIT_SCROLL_NEWS_BOARD)
    public CommonResult<String> hideNews(@PathVariable int newsId) {
        newsService.hideNews(newsId);
        return CommonResult.success("隐藏成功");
    }

    @PutMapping ("/notShownNews/{newsId}")
    @AccessPermission(PermissionEnum.EDIT_SCROLL_NEWS_BOARD)
    public CommonResult<String> showNews(@PathVariable int newsId) {
        newsService.showNews(newsId);
        return CommonResult.success("显示成功");
    }

    @DeleteMapping("/{newsId}")
    @AccessPermission(PermissionEnum.EDIT_SCROLL_NEWS_BOARD)
    public CommonResult<String> deleteNews(@PathVariable int newsId) {
        newsService.deleteNews(newsId);
        return CommonResult.success("删除成功");
    }
}
