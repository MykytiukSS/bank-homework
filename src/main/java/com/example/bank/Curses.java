package com.example.bank;

import javax.persistence.*;

@Entity
@Table(name = "curses")

public class Curses {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // or @GeneratedValue
    @Column(name = "Currency_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "currency")
    private Double currency;

    public Curses() {
    }

    public Curses(String name, Double currency) {
        this.name = name;
        this.currency = currency;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
