package com.example.wedding.repo;

import com.example.wedding.domain.ScenarioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScenarioItemRepository extends JpaRepository<ScenarioItem, Long> {
    List<ScenarioItem> findByScenarioId(Long scenarioId);
    void deleteByScenarioIdAndPrestationId(Long scenarioId, Long prestationId);
    void deleteByScenarioId(Long scenarioId);
    
    boolean existsByScenarioIdAndPrestationId(Long scenarioId, Long prestationId);
    
    @Modifying
    @Query("DELETE FROM ScenarioItem si WHERE si.prestation.id = :prestationId")
    void deleteByPrestationId(@Param("prestationId") Long prestationId);

}
