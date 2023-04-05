package com.softeng.dingtalk.po_entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "news")
public class News {
    /**
     * newsId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告跳转链接
     */
    private String link;

    /**
     * 公告发布作者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private User author;

    /**
     * 公告是否被删除
     */
    private int isDeleted;

    /**
     * 公告是否显示到首页
     */
    private int isShown;

    /**
     * 公告发布时间
     */
    private LocalDateTime releaseTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        News news = (News) o;
        return Objects.equals(id, news.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
