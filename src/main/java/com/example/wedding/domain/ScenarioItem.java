package com.example.wedding.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ScenarioItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Scenario scenario;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Prestation prestation;
}
