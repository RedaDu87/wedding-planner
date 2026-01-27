package com.example.wedding.web;

import com.example.wedding.domain.Scenario;
import com.example.wedding.domain.ScenarioItem;
import com.example.wedding.service.WeddingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WeddingApiController {

    private final WeddingService service;

    public record CreateScenarioRequest(
            @NotBlank(message = "Le nom du scénario est obligatoire")
            @Size(max = 255, message = "Le nom ne doit pas dépasser 255 caractères")
            String nom
    ) {}
    public record ScenarioView(Long scenarioId, List<ScenarioItemDto> items, BigDecimal total) {}
    public record ScenarioItemDto(Long prestationId, String titre, BigDecimal prix) {}

    @PostMapping("/scenarios")
    public Scenario createScenario(@Valid @RequestBody CreateScenarioRequest req) {
        return service.createScenario(req.nom());
    }

    @PostMapping("/scenarios/{scenarioId}/items")
    public ScenarioView addItem(@PathVariable Long scenarioId, @RequestParam Long prestationId) {
        service.addPrestationToScenario(scenarioId, prestationId);
        return scenarioView(scenarioId);
    }

    @DeleteMapping("/scenarios/{scenarioId}/items")
    public ScenarioView removeItem(@PathVariable Long scenarioId, @RequestParam Long prestationId) {
        service.removePrestationFromScenario(scenarioId, prestationId);
        return scenarioView(scenarioId);
    }

    @GetMapping("/scenarios/{scenarioId}")
    public ScenarioView scenarioView(@PathVariable Long scenarioId) {
        List<ScenarioItem> items = service.items(scenarioId);
        BigDecimal total = service.total(scenarioId);

        List<ScenarioItemDto> dtos = items.stream()
                .map(i -> new ScenarioItemDto(
                        i.getPrestation().getId(),
                        i.getPrestation().getTitre(),
                        i.getPrestation().getPrix()
                ))
                .toList();

        return new ScenarioView(scenarioId, dtos, total);
    }
}
