package com.example.wedding.web;

import com.example.wedding.domain.Prestation;
import com.example.wedding.domain.Scenario;
import com.example.wedding.service.WeddingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminApiController {

    private final WeddingService service;

    // ---- Prestations CRUD ----
    public record PrestationReq(
            @NotBlank(message = "Le titre est obligatoire")
            @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
            String titre,
            
            @Size(max = 2000, message = "La description ne doit pas dépasser 2000 caractères")
            String description,
            
            @NotNull(message = "Le prix est obligatoire")
            @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à 0")
            @Digits(integer = 10, fraction = 2, message = "Le prix doit avoir au maximum 10 chiffres entiers et 2 décimales")
            BigDecimal prix
    ) {}

    public record PrestationDto(
            Long id,
            String titre,
            String description,
            BigDecimal prix
    ) {}

    @GetMapping("/prestations")
    public List<PrestationDto> prestations() {
        return service.prestations().stream()
                .map(p -> new PrestationDto(
                        p.getId(),
                        p.getTitre(),
                        p.getDescription(),
                        p.getPrix()
                ))
                .toList();
    }

    @PostMapping("/prestations")
    public PrestationDto createPrestation(@Valid @RequestBody PrestationReq req) {
        Prestation p = service.createPrestation(
                req.titre(),
                req.description(),
                req.prix()
        );
        return new PrestationDto(
                p.getId(),
                p.getTitre(),
                p.getDescription(),
                p.getPrix()
        );
    }

    @PutMapping("/prestations/{id}")
    public PrestationDto updatePrestation(
            @PathVariable Long id,
            @Valid @RequestBody PrestationReq req
    ) {
        Prestation p = service.updatePrestation(
                id,
                req.titre(),
                req.description(),
                req.prix()
        );
        return new PrestationDto(
                p.getId(),
                p.getTitre(),
                p.getDescription(),
                p.getPrix()
        );
    }

    @DeleteMapping("/prestations/{id}")
    public void deletePrestation(@PathVariable Long id) {
        service.deletePrestation(id);
    }

    // ---- Scénarios ----
    public record ScenarioReq(
            @NotBlank(message = "Le nom du scénario est obligatoire")
            @Size(max = 255, message = "Le nom ne doit pas dépasser 255 caractères")
            String nom
    ) {}
    public record ScenarioDto(Long id, String nom) {}

    @DeleteMapping("/scenarios/{id}")
    public void deleteScenario(@PathVariable Long id) {
        service.deleteScenario(id);
    }

    @PutMapping("/scenarios/{id}")
    public ScenarioDto renameScenario(@PathVariable Long id, @Valid @RequestBody ScenarioReq req) {
        Scenario s = service.updateScenario(id, req.nom());
        return new ScenarioDto(s.getId(), s.getNom());
    }
}
