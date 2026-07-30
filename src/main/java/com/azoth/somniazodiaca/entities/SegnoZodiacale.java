package com.azoth.somniazodiaca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "segni_zodiacali")
@Data
@SuperBuilder
@NoArgsConstructor
public class SegnoZodiacale extends BaseEntity {

}
