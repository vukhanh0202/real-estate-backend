package com.uit.realestate.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.action.Comment;
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
    private String gender;

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
    private List<Comment> comments = new ArrayList<>();

}
