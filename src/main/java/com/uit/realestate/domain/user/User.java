package com.uit.realestate.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uit.realestate.constant.enums.user.EGender;
import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.action.Comment;
import com.uit.realestate.domain.action.Favourite;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.tracking.TrackingCategory;
import com.uit.realestate.domain.tracking.TrackingDistrict;
import com.uit.realestate.domain.tracking.TrackingProvince;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "`user`")
public class User extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    @Column(unique = true)
    private String email;
    private String fullName;
    private String firstName;
    private String lastName;
    private String avatar;
    private String phone;
    private String description;
    private EGender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,
            orphanRemoval = true,fetch = FetchType.LAZY)
    private UserAddress userAddress;

    public void setUserAddress(UserAddress userAddress) {
        if (userAddress == null) {
            if (this.userAddress != null) {
                this.userAddress.setUser(null);
            }
        }
        else {
            userAddress.setUser(this);
        }
        this.userAddress = userAddress;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<TrackingCategory> trackingCategories = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<TrackingProvince> trackingProvinces = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<TrackingDistrict> trackingCountries = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,
            orphanRemoval = true, mappedBy = "user")
    private List<Favourite> favourites = new ArrayList<>();

    //Constructors, getters and setters removed for brevity
    public void addFavourite(Favourite favourite) {
        if (favourites == null){
            favourites = new ArrayList<>();
        }
        favourites.add(favourite);
        favourite.setUser(this);
    }

    public void removeFavourite(Favourite favourite) {
        favourites.remove(favourite);
        favourite.setUser(null);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<Apartment> apartments = new ArrayList<>();

    public User(Long id) {
        this.id = id;
    }
}
