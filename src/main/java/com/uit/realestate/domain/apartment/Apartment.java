package com.uit.realestate.domain.apartment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uit.realestate.constant.enums.ETypeApartment;
import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.action.Comment;
import com.uit.realestate.domain.apartment.join.ApartmentTag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.*;

@NoArgsConstructor
@Entity
@Table(name = "apartment")
@Data
public class Apartment extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String code;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double area;

    private Double price;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private ETypeApartment typeHouse;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private Date expiredDate;

    @OneToOne(mappedBy = "apartment",cascade = CascadeType.ALL,
            orphanRemoval = true,fetch = FetchType.LAZY)
    private ApartmentAddress houseAddress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "apartment")
    private Set<ApartmentTag> apartmentTags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "apartment")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
