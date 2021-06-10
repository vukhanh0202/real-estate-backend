package com.uit.realestate.domain.apartment;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.tracking.TrackingCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Apartment> apartments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<TrackingCategory> trackingCategories = new ArrayList<>();

    public Category(Long id) {
        this.id = id;
    }
}
