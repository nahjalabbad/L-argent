package org.example.largent.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TeenInfo {
    private String name;
    private LocalDate birthDate;
    private Double budget;
    private Double weeklyLimit;
    private Double currentSpending;
}
