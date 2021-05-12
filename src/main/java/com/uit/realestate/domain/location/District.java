package com.uit.realestate.domain.location;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "district")
public class District{

    @Id
    private Long id;

    @Column
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", foreignKey = @javax.persistence.ForeignKey(name = "none"))
    private Province province;
}
