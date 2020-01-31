package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 1/30/2020 6:10 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(name = "index_dc", columnList = "dcRecordId", unique = false),
                  @Index(name = "index_ac", columnList = "acRecordId", unique = true)})
public class DcAcRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int dcRecordId;
    private int acRecordId;


    public DcAcRelation(int dcId, int acId) {
        this.dcRecordId = dcId;
        this.acRecordId = acId;
    }
}
