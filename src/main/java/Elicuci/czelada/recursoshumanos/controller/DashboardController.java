package Elicuci.czelada.recursoshumanos.controller;

import Elicuci.czelada.recursoshumanos.dto.DashboardDTO;
import Elicuci.czelada.recursoshumanos.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard(
            @RequestParam(required = false) Long sucursalId) {
        return ResponseEntity.ok(dashboardService.getDashboardDTO(sucursalId));
    }
}
