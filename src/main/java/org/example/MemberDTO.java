package org.example;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter @Getter
public class MemberDTO {

    private String name;
    private Integer age;
}
