package com.nexters.phochak.domain;

import javax.persistence.*;

@Entity
@Table(name = "`USER`")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;
}
