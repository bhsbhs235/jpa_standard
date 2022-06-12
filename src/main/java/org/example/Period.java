package org.example;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
