package com.uit.realestate.domain.house;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uit.realestate.constant.enums.ETypeHouse;
import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.action.Comment;
import com.uit.realestate.domain.house.join.HouseTag;
import com.uit.realestate.domain.user.Role;
import com.uit.realestate.domain.user.UserAddress;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.*;

@NoArgsConstructor
@Entity
@Table(name = "house")
@Data
public class House extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String code;

    private String title;

    private String overview;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double area;

    private Double price;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private ETypeHouse typeHouse;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private Date expiredDate;

    @OneToOne(mappedBy = "house",cascade = CascadeType.ALL,
            orphanRemoval = true,fetch = FetchType.LAZY)
    private HouseAddress houseAddress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "house")
    private Set<HouseTag> houseTags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "house")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
