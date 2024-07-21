package com.fin.finfintech.domain;

import com.fin.finfintech.exception.AccountException;
import com.fin.finfintech.type.AccountStatus;
import com.fin.finfintech.type.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AccountUser accountUser;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    //중요한 변수를 변경하는 것은 객체 내에서..
    public void useBalance(Long amount) {
        if (amount > this.balance) {
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }
        this.balance -= amount;
    }

    public void cancelBalance(Long amount) {
        if (amount < 0) {
            throw new AccountException(ErrorCode.INVALID_REQUEST);
        }
        this.balance += amount;
    }

    public void deposit(Long amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public void withdraw(Long amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
        }
    }


}
