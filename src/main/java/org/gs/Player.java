package org.gs;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Player extends PanacheEntity {

    @Column(length = 100)
    public String name;

    @Column(length = 100)
    public String email;

    @Column(length = 100)
    public String wallet;

    
}
