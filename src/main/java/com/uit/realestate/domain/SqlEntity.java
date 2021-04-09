package com.uit.realestate.domain;

import com.uit.realestate.domain.user.User;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class SqlEntity implements Serializable {

    protected static final long serialVersionUID = 1L;

    @Column(columnDefinition = "boolean default false")
    protected boolean isDeleted;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by",foreignKey = @javax.persistence.ForeignKey(name = "none"))
    protected User createdBy;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by",foreignKey = @javax.persistence.ForeignKey(name = "none"))
    protected User updatedBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt = new Date();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedAt = new Date();

}
