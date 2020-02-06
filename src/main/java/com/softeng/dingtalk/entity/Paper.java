package com.softeng.dingtalk.entity;

        import com.fasterxml.jackson.annotation.JsonIgnore;
        import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
        import com.softeng.dingtalk.vo.PaperVO;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;
        import lombok.ToString;

        import javax.persistence.*;
        import java.time.LocalDate;
        import java.time.LocalDateTime;
        import java.util.List;

/**
 * @author zhanyeye
 * @description 论文实体类
 * @create 2/5/2020 4:33 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@NamedEntityGraph(name="paper.graph",attributeNodes={@NamedAttributeNode("paperDetails")})
public class Paper {
    //定义静态常量表示用户权限
    public static final int WAIT = 0;
    public static final int ACCEPT = 1;
    public static final int REJECT= 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String journal;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;  //插入时间
    private int level;
    private int result;

    @JsonIgnoreProperties("paper")
    @OneToMany(mappedBy = "paper")
    private List<PaperDetail> paperDetails;

    public Paper(int id) {
        this.id = id;
    }

    public Paper(PaperVO paperVO) {
        this.title = paperVO.getTitle();
        this.journal = paperVO.getJournal();
        this.level = paperVO.getLevel();
    }

    public void update(PaperVO paperVO) {
        this.title = paperVO.getTitle();
        this.journal = paperVO.getJournal();
        this.level = paperVO.getLevel();
    }


}
