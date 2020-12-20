package com.serob.cafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Represents basic Entity with fields <i>(id, added and updated dates))</i> used in all derivatives
 */
@MappedSuperclass
//From jackson  v2.12.0 there's an annotation @JsonIncludeProperties
//See: https://github.com/FasterXML/jackson-databind/issues/1296
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @JsonIgnore
    private Date addedDate;

    @UpdateTimestamp
    @JsonIgnore
    private Date updatedDate;

    public Long getId() {
        return id;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }
}
