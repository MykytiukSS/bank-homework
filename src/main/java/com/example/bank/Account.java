package com.example.bank;


import javax.persistence.*;

@Entity
@Table(name = "Accounts")

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // or @GeneratedValue
    @Column(name = "Account_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "Client's_Id")
    private Users user;


    @Column(name = "valuta")
    private String valuta;

    @Column(name = "Sum")
    private Double sum;


    public Account() {
    }

    public Account(Users user, String valuta, Double sum) {
        this.user = user;
        this.valuta = valuta;
        this.sum = sum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
