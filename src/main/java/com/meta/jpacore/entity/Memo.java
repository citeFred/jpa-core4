package com.meta.jpacore.entity;

import jakarta.persistence.*;
import lombok.Setter;

@Setter
@Entity
@Table(name = "memo")
public class Memo {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable = null 허용 여부
    // unique = 중복 허용 여부(고유)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "contents", nullable = false, length = 500)
    private String contents;
}
