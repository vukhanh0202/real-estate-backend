package com.uit.realestate.domain;

import com.uit.realestate.repository.converter.ListToLongConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tracking_temporary_chat")
public class TrackingTemporaryChat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String key;

    @Column
    @Convert(converter = ListToLongConverter.class)
    private List<Long> value;
}

