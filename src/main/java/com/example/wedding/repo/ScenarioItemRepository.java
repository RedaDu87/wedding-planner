package com.example.wedding.repo;

import com.example.wedding.domain.ScenarioItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioItemRepository extends JpaRepository<ScenarioItem, Long> {
    List<ScenarioItem> findByScenarioId(Long scenarioId);
    void deleteByScenarioIdAndPrestationId(Long scenarioId, Long prestationId);
    void deleteByScenarioId(Long scenarioId);

}
