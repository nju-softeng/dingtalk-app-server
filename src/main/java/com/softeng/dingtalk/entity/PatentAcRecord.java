package com.softeng.dingtalk.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Proxy(lazy = false)
@Table(name = "patent_ac_record")
public class PatentAcRecord {

    public static final int AuditorType = 1;
    public static final int AuthorizationType = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "patent_id")
    private int patentId;

    @Column(name = "ac_record_id")
    private int acRecordId;

    @Column(name = "type")
    private int type;
}
