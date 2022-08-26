package com.example.bank;


import javax.persistence.*;

@Entity
@Table(name = "Transactions")

public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // or @GeneratedValue
    @Column(name = "Transaction_id")
    private Long id;


    private Double amount;



    @ManyToOne
    @JoinColumn(name = "from")
    private  Account fromAcc;

    @ManyToOne
    @JoinColumn(name = "to")
    private  Account toAcc;


    public Transactions(Double amount, Account toAcc) {
        this.amount = amount;
        this.toAcc = toAcc;
    }

    public Transactions() {
    }

    public Transactions(Double amount, Account fromAcc, Account toAcc) {
        this.amount = amount;
        this.fromAcc = fromAcc;
        this.toAcc = toAcc;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", amount=" + amount +
                ", fromAcc=" + fromAcc +
                ", toAcc=" + toAcc +
                '}';
    }
}
