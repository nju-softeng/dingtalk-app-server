package com.softeng.dingtalk.entity;

import com.softeng.dingtalk.enums.Position;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 5/28/2020 6:20 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Position position = Position.UNDERGRADUATE;

}
