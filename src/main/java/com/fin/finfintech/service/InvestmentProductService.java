package com.fin.finfintech.service;

import com.fin.finfintech.domain.InvestmentProduct;
import com.fin.finfintech.dto.InvestmentProductDto;
import com.fin.finfintech.repository.InvestmentProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvestmentProductService {

    private final InvestmentProductRepository investmentProductRepository;

    /**
     * 모든 투자 상품을 가져옵니다.
     *
     * @return 모든 투자 상품 DTO 리스트
     */
    public List<InvestmentProductDto> getAllInvestmentProducts() {
        return investmentProductRepository.findAll().stream()
                .map(this::convertToDto) // InvestmentProduct 객체를 InvestmentProductDto로 변환
                .collect(Collectors.toList());
    }

    /**
     * ID로 투자 상품 정보를 가져옵니다.
     *
     * @param id 투자 상품 ID
     * @return 해당 ID의 투자 상품 정보
     */
    public Optional<InvestmentProductDto> getInvestmentProductById(Long id) {
        return investmentProductRepository.findById(id)
                .map(this::convertToDto); // 존재하는 경우 DTO로 변환
    }

    /**
     * 새로운 투자 상품을 생성합니다.
     *
     * @param investmentProductDto 투자 상품 정보 DTO
     * @return 생성된 투자 상품 정보 DTO
     */
    public InvestmentProductDto createInvestmentProduct(InvestmentProductDto investmentProductDto) {
        InvestmentProduct investmentProduct = convertToEntity(investmentProductDto); // DTO를 엔티티로 변환
        InvestmentProduct savedProduct = investmentProductRepository.save(investmentProduct); // 데이터베이스에 저장
        return convertToDto(savedProduct); // 저장된 엔티티를 DTO로 변환하여 반환
    }

    /**
     * ID로 투자 상품을 삭제합니다.
     *
     * @param id 투자 상품 ID
     */
    public void deleteInvestmentProduct(Long id) {
        investmentProductRepository.deleteById(id); // 해당 ID의 투자 상품 삭제
    }

    // DTO와 Entity 간 변환 메서드
    private InvestmentProductDto convertToDto(InvestmentProduct investmentProduct) {
        return InvestmentProductDto.builder()
                .id(investmentProduct.getId())
                .name(investmentProduct.getName())
                .description(investmentProduct.getDescription())
                .riskLevel(investmentProduct.getRiskLevel())
                .expectedReturnRate(investmentProduct.getExpectedReturnRate())
                .build();
    }

    private InvestmentProduct convertToEntity(InvestmentProductDto investmentProductDto) {
        InvestmentProduct investmentProduct = new InvestmentProduct();
        investmentProduct.setName(investmentProductDto.getName());
        investmentProduct.setDescription(investmentProductDto.getDescription());
        investmentProduct.setRiskLevel(investmentProductDto.getRiskLevel());
        investmentProduct.setExpectedReturnRate(investmentProductDto.getExpectedReturnRate());
        // 추가적인 속성 설정이 필요할 수 있습니다.
        return investmentProduct;
    }
}
