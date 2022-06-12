package org.example;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass // 공통적인 속성 써야할 때 create_date, update_date 같은거
public abstract class BaseEntity {

    private LocalDateTime createdDate;
}

/*
    상속 관게 매핑 아님
    엔티티 아님(조회 불가),
    자식 클래스에 매핑 정보만 제공(공통으로 필요한 컬럼이 있을 때 주로 사용)
    추상클래스로 사용(실제 존재하는 테이블이 아니기 때문에)
 */