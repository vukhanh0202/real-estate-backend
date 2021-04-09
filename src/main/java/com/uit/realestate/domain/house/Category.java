package com.uit.realestate.domain.house;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.action.Comment;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<House> houses = new ArrayList<>();
}
