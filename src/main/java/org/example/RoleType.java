package org.example;

// EnumType.ORDINAL로 하면 USER 값이 0 ADMIN 값이 1 즉, 순서대로 들어감 이렇게 되면 치명적인 단점이 있는데,
// 개발 중간에 GUEST가 들어가면 이전에는 USER가 0(이미 저장됨) GUEST가 0으로 생성되기 때문에 알맞게 맞춰줘야하는 상황이 발생한다
public enum RoleType {
    USER, ADMIN
    //GUEST, USER, ADMIN
}
