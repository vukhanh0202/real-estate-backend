package com.uit.realestate.domain.location;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "country")
public class Country{

    @Id
    private String code;

    @Column
    private String name;

    @Column(name = "phone_code")
    private Integer phoneCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    private List<Province> provinces = new ArrayList<>();
}

