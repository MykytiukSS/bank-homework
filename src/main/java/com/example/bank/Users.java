package com.example.bank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")

public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // or @GeneratedValue
    @Column(name = "User_id")
    private Long id;


    @Column(name = "Name",nullable = false)
    private String name;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Account> accounts= new ArrayList<>();

    public Users() {
    }

    public Users(String name) {
        this.name = name;
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

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
