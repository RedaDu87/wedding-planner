package com.example.wedding.service;

import com.example.wedding.domain.*;
import com.example.wedding.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeddingService {

    private final PrestationRepository prestationRepo;
    private final ScenarioRepository scenarioRepo;
    private final ScenarioItemRepository itemRepo;

    public List<Prestation> prestations() {
        return prestationRepo.findAll();
    }

    public List<Scenario> scenarios() {
        return scenarioRepo.findAll();
    }

    public List<ScenarioItem> items(Long scenarioId) {
        return itemRepo.findByScenarioId(scenarioId);
    }

    public BigDecimal total(Long scenarioId) {
        return itemRepo.findByScenarioId(scenarioId).stream()
                .map(i -> i.getPrestation().getPrix())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Scenario createScenario(String nom) {
        return scenarioRepo.save(Scenario.builder().nom(nom).build());
    }

    @Transactional
    public void addPrestationToScenario(Long scenarioId, Long prestationId) {
        // Check if the prestation is already in this scenario
        if (itemRepo.existsByScenarioIdAndPrestationId(scenarioId, prestationId)) {
            throw new IllegalArgumentException("Cette prestation est déjà présente dans ce scénario");
        }
        
        Scenario scenario = scenarioRepo.findById(scenarioId)
                .orElseThrow(() -> new IllegalArgumentException("Scenario introuvable"));
        Prestation prestation = prestationRepo.findById(prestationId)
                .orElseThrow(() -> new IllegalArgumentException("Prestation introuvable"));

        itemRepo.save(ScenarioItem.builder()
                .scenario(scenario)
                .prestation(prestation)
                .build());
    }

    @Transactional
    public void removePrestationFromScenario(Long scenarioId, Long prestationId) {
        itemRepo.deleteByScenarioIdAndPrestationId(scenarioId, prestationId);
    }

    @Transactional
    public void deleteScenario(Long scenarioId) {
        itemRepo.deleteByScenarioId(scenarioId);
        scenarioRepo.deleteById(scenarioId);

    }

    @Transactional
    public void deletePrestation(Long prestationId) {
        // Use optimized query to delete all items pointing to this prestation
        itemRepo.deleteByPrestationId(prestationId);
        prestationRepo.deleteById(prestationId);
    }

    @Transactional
    public Prestation createPrestation(String titre, String description, BigDecimal prix) {
        return prestationRepo.save(
                Prestation.builder()
                        .titre(titre)
                        .description(description)
                        .prix(prix)
                        .build()
        );
    }

    @Transactional
    public Prestation updatePrestation(Long id, String titre, String description, BigDecimal prix) {
        Prestation p = prestationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prestation introuvable"));

        p.setTitre(titre);
        p.setDescription(description);
        p.setPrix(prix);

        return prestationRepo.save(p);
    }


    @Transactional
    public Scenario updateScenario(Long id, String nom) {
        Scenario s = scenarioRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Scenario introuvable"));
        s.setNom(nom);
        return scenarioRepo.save(s);
    }

}
