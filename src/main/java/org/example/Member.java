package org.example;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
// @Table(name="USER") 테이블 지정
public class Member {

    @Id // PK
    private Long id;

    // @Column(name = "user_name") 컬럼지정
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
