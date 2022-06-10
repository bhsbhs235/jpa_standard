package org.example;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A") // 없으면 default 클래스 이름
public class Book extends Item{
    private String Author;
}
