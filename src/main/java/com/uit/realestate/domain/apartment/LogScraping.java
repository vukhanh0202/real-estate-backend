package com.uit.realestate.domain.apartment;

import com.uit.realestate.domain.SqlEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "log_scraping")
@Data
public class LogScraping extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long idApartment;
    private String titleApartment;
    private String linkScraping;
    private boolean status;
    private String error;
}
