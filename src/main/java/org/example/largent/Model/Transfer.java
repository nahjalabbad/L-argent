package org.example.largent.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transferId;

    @NotNull(message = "user id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer userId;

    @NotNull(message = "Teen id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer teenId;

    @NotNull(message = "amount cannot be empty")
    @Positive
    @Column(columnDefinition = "double not null")
    private Double amount;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime transferTime;

    @NotEmpty(message = "purpose cannot be empty")
    @Column(columnDefinition = "varchar(50) not null")
    private String purpose;

    @Pattern(regexp = "^(USD|EUR)$")
    @Column(columnDefinition = "varchar(20) check(currency='USD' or currency='EUR')")
    private String currency;
}
