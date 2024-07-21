package com.fin.finfintech.controller;

import com.fin.finfintech.dto.InvestmentProductDto;
import com.fin.finfintech.service.InvestmentProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/investment/product")
@AllArgsConstructor
public class InvestmentProductController {

    private final InvestmentProductService investmentProductService;


    /**
     * 새로운 투자 상품을 생성합니다.
     *
     * @param investmentProductDto 투자 상품 정보 DTO
     * @return 생성된 투자 상품 DTO
     */
    @PostMapping
    public ResponseEntity<InvestmentProductDto> createInvestmentProduct(@RequestBody InvestmentProductDto investmentProductDto) {
        InvestmentProductDto createdProduct = investmentProductService.createInvestmentProduct(investmentProductDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * 모든 투자 상품 정보를 가져옵니다.
     *
     * @return 모든 투자 상품 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<List<InvestmentProductDto>> getAllInvestmentProducts() {
        List<InvestmentProductDto> products = investmentProductService.getAllInvestmentProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * ID로 특정 투자 상품 정보를 가져옵니다.
     *
     * @param id 투자 상품 ID
     * @return 특정 투자 상품 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvestmentProductDto> getInvestmentProductById(@PathVariable("id") Long id) {
        Optional<InvestmentProductDto> productDto = investmentProductService.getInvestmentProductById(id);
        return productDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * ID로 투자 상품을 삭제합니다.
     *
     * @param id 투자 상품 ID
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestmentProduct(@PathVariable("id") Long id) {
        investmentProductService.deleteInvestmentProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
