package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.convertor.NewsConvertor;
import com.softeng.dingtalk.dao.repository.NewsRepository;
import com.softeng.dingtalk.dto.req.NewsReq;
import com.softeng.dingtalk.dto.resp.NewsResp;
import com.softeng.dingtalk.enums.NewsState;
import com.softeng.dingtalk.po_entity.News;
import com.softeng.dingtalk.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class NewsService {
    @Resource
    private NewsRepository newsRepository;
    @Resource
    private NewsConvertor newsConvertor;

    public Map<String, Object> getShownNews(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("release_time"));
        Page<News> newsPage = newsRepository.findAllByIsShownAndIsDeleted(
                NewsState.IS_SHOWN.getValue(),
                NewsState.IS_NOT_DELETED.getValue(),
                pageable);
        List<NewsResp> newsList = StreamUtils.map(newsPage.toList(), news -> newsConvertor.entity_PO2Resp(news));
        return Map.of("newsList", newsList, "total", newsPage.getTotalElements());
    }

    public Map<String, Object> getNotShownNews(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("release_time"));
        Page<News> newsPage = newsRepository.findAllByIsShownAndIsDeleted(
                NewsState.IS_NOT_SHOWN.getValue(),
                NewsState.IS_NOT_DELETED.getValue(),
                pageable);
        List<NewsResp> newsList = StreamUtils.map(newsPage.toList(), news -> newsConvertor.entity_PO2Resp(news));
        return Map.of("newsList", newsList, "total", newsPage.getTotalElements());
    }

    public List<NewsResp> getShownNews() {
        return StreamUtils.map(newsRepository.findAllByIsShownAndIsDeleted(
                NewsState.IS_SHOWN.getValue(),
                NewsState.IS_NOT_DELETED.getValue()), news -> newsConvertor.entity_PO2Resp(news));
    }

    public Map<String, Object> getAllNews(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("release_time"));
        Page<News> newsPage = newsRepository.findAllByIsDeleted(NewsState.IS_NOT_DELETED.getValue(), pageable);
        List<NewsResp> newsList = StreamUtils.map(newsPage.toList(), news -> newsConvertor.entity_PO2Resp(news));
        return Map.of("newsList", newsList, "total", newsPage.getTotalElements());
    }

    public void addNews(NewsReq newsReq) {
        newsRepository.save(newsConvertor.req2Entity_PO(newsReq));
    }

    public void hideNews(int newsId) {
        newsRepository.updateIsShown(newsId, NewsState.IS_NOT_SHOWN.getValue());
    }

    public void showNews(int newsId) {
        newsRepository.updateIsShown(newsId, NewsState.IS_SHOWN.getValue());
    }

    public void deleteNews(int newsId) {
        newsRepository.updateIsDeleted(newsId, NewsState.IS_DELETED.getValue());
    }
}
