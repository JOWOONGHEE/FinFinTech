package com.fin.finfintech.service;

import com.fin.finfintech.domain.Account;
import com.fin.finfintech.domain.Investment;
import com.fin.finfintech.domain.InvestmentProduct;
import com.fin.finfintech.dto.InvestmentDto;
import com.fin.finfintech.repository.AccountRepository;
import com.fin.finfintech.repository.InvestmentProductRepository;
import com.fin.finfintech.repository.InvestmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final InvestmentProductRepository investmentProductRepository;
    private final AccountRepository accountRepository;

    public List<InvestmentDto> getAllInvestments() {
        return investmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<InvestmentDto> getInvestmentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("투자 ID가 존재하지 않습니다.");
        }
        return investmentRepository.findById(id)
                .map(this::convertToDto);
    }

    public InvestmentDto createInvestment(InvestmentDto investmentDto) {
        if (investmentDto.getProductId() == null) {
            throw new IllegalArgumentException("투자 상품 ID가 존재하지 않습니다.");
        }
        if (investmentDto.getAccountNumber() == null) {
            throw new IllegalArgumentException("계좌 번호가 존재하지 않습니다.");
        }
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(investmentDto.getAccountNumber());
        Account account = optionalAccount.orElseThrow(() -> new IllegalArgumentException("계좌가 존재하지 않습니다."));

        if (investmentDto.getAmount() > account.getBalance()) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        InvestmentProduct product = investmentProductRepository.findById(investmentDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 투자 상품이 존재하지 않습니다."));

        Investment investment = convertToEntity(investmentDto);
        investment.setProduct(product); // 투자 상품 설정

        Investment savedInvestment = investmentRepository.save(investment);

        account.setBalance(account.getBalance() - investmentDto.getAmount());
        accountRepository.save(account);

        return convertToDto(savedInvestment);
    }

    private Investment convertToEntity(InvestmentDto investmentDto) {
        Investment investment = new Investment();
        investment.setAccountNumber(investmentDto.getAccountNumber());
        investment.setAmount(investmentDto.getAmount());
        investment.setStartDate(investmentDto.getStartDate());
        investment.setEndDate(investmentDto.getEndDate());
        return investment;
    }

    public void deleteInvestment(Long id) {
        if (!investmentRepository.existsById(id)) {
            throw new IllegalArgumentException("투자 ID가 존재하지 않습니다.");
        }
        investmentRepository.deleteById(id);
    }

    private InvestmentDto convertToDto(Investment investment) {
        return InvestmentDto.builder()
                .id(investment.getId())
                .accountNumber(investment.getAccountNumber())
                .productId(investment.getProduct().getId())
                .amount(investment.getAmount())
                .startDate(investment.getStartDate())
                .endDate(investment.getEndDate())
                .build();
    }
}

