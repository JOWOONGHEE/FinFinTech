package com.fin.finfintech.controller;

import com.fin.finfintech.dto.InvestmentDto;
import com.fin.finfintech.service.InvestmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/investment")
@AllArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    /**
     * 새로운 투자를 생성합니다.
     *
     * @param investmentDto 투자 정보 DTO
     * @return 생성된 투자 정보
     */
    @PostMapping
    public ResponseEntity<InvestmentDto> createInvestment(@RequestBody InvestmentDto investmentDto) {
        InvestmentDto createdInvestment = investmentService.createInvestment(investmentDto); // 서비스에서 새로운 투자 생성
        return ResponseEntity.ok(createdInvestment); // 생성된 투자 정보를 200 OK 응답으로 반환
    }

    /**
     * 모든 투자 목록을 가져옵니다.
     *
     * @return 모든 투자 목록
     */
    @GetMapping
    public List<InvestmentDto> getAllInvestments() {
        return investmentService.getAllInvestments(); // 서비스에서 모든 투자 정보를 가져옵니다.
    }

    /**
     * 특정 ID에 해당하는 투자를 가져옵니다.
     *
     * @param id 투자 ID
     * @return 해당 ID의 투자 정보 또는 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvestmentDto> getInvestmentById(@PathVariable("id") Long id) {
        return investmentService.getInvestmentById(id)
                .map(ResponseEntity::ok) // 투자가 존재할 경우 200 OK 응답
                .orElse(ResponseEntity.notFound().build()); // 존재하지 않을 경우 404 Not Found 응답
    }



    /**
     * 특정 ID에 해당하는 투자를 삭제합니다.
     *
     * @param id 투자 ID
     * @return 204 No Content 응답
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable("id") Long id) {
        investmentService.deleteInvestment(id); // 서비스에서 해당 ID의 투자 삭제
        return ResponseEntity.noContent().build(); // 삭제가 완료되면 204 No Content 응답
    }
}
