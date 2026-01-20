package com.example.wedding.repo;

import com.example.wedding.domain.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {}
