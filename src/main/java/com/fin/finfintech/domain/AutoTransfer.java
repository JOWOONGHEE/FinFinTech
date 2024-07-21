package com.fin.finfintech.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AutoTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_account_number")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_number")
    private Account toAccount;

    private Long amount;
    private String memo;
    private LocalDateTime transferDate;

    private LocalDateTime scheduleStartDateTime;
    private String frequency;

    // 추가적으로 필요한 필드나 메서드가 있다면 여기에 추가
}
