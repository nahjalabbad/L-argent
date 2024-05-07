package org.example.largent.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rewardId;

    @NotEmpty(message = "Reward title cannot be empty")
    @Column(columnDefinition = "varchar(25) not null")
    private String title;

    @NotNull(message = "Required points cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer pointsRequired;

    @Pattern(regexp = "^(expired|not expired)$")
    @Column(columnDefinition = "varchar(15) check(status='expired' or status='not expired')")
    private String status;

    @Column(columnDefinition = "DATE")
    private LocalDate expireDate;

    @Column(columnDefinition = "int")
    private Integer userID;
}
