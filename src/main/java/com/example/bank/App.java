package com.example.bank;


import javax.persistence.*;
import javax.transaction.*;
import javax.transaction.RollbackException;
import javax.transaction.xa.XAResource;
import java.util.Currency;
import java.util.List;
import java.util.Scanner;

public class App
{
    private static final Scanner sc = new Scanner(System.in);
    private static EntityManager em;

    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPATest");
            em = emf.createEntityManager();

            rates_init();

            try {
                while (true) {
                    System.out.println("1: add new client");
                    System.out.println("2: add new account");
                    System.out.println("3: view all clients");
                    System.out.println("4: add money to user account");
                    System.out.println("5: transfer money");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addUser();
                            break;
                        case "2":
                            addAccount();
                            break;
                        case "3":
                            viewUsers();
                            break;
                        case "4":
                            addMoneyToAccount();
                            break;
                        case "5":
                            transferMoney();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                em.close();
                emf.close();
                sc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void transferMoney() {
        Query query3;
        Double amont;

        System.out.println("Please, enter account Id:");
        String searchYourAccountId = sc.nextLine();

        Account accountFrom = em.find(Account.class, Long.parseLong(searchYourAccountId));
        if (accountFrom == null) {
            System.out.println("Account not found!");
            return;
        }

        System.out.println("Enter receiver's account Id:");
        String receiverAccountId = sc.nextLine();

        Account receiverAccount = em.find(Account.class, Long.parseLong(receiverAccountId));
        if (receiverAccount == null) {
            System.out.println("Account not found !");
            return;
        }

        System.out.println("Enter Amount to transfer:");
        String sAmount = sc.nextLine();
        int amount = Integer.parseInt(sAmount);
        Double amountD = Double.valueOf(amount);
        if (amount > accountFrom.getSum()) {
            System.out.println("Not enough money!");
            return;
        }

        em.getTransaction().begin();
        try {
            Transactions transaction = new Transactions(amountD, accountFrom, receiverAccount);
            em.persist(transaction);

            if (accountFrom.getValuta() != receiverAccount.getValuta()) {

                if (accountFrom.getValuta() == "UAH") {
                    amountD = amountD;
                } else if (accountFrom.getValuta() == "USD") {


                    amountD = amountD * 39;
                } else {
                    amountD = amountD * 41;
                }
            }
            Double temp = Double.valueOf(accountFrom.getSum());
            accountFrom.setSum(temp - amountD);

            Double temp1 = Double.valueOf(receiverAccount.getSum());
            receiverAccount.setSum(temp + amountD);

            em.getTransaction().commit();
            System.out.println("Money Transferred!");


        }    catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
            return;
        }
    }

    private static void viewUsers() {
        try {
            Query query1 = em.createQuery("SELECT u FROM Users u ORDER BY u.name", Users.class);
            List<Users> userList = query1.getResultList();

            for (Users user : userList) {
                System.out.println(user);
            }
        } catch (NoResultException ex) {
            System.out.println("There are no users!");
            return;
        }
    }

    private static void addMoneyToAccount() {
        Double ammount = 0.0;
        Query query2;

        System.out.println("Please, enter account Id:");
        String searchAccountId = sc.nextLine();

        Account account = em.find(Account.class, Long.parseLong(searchAccountId));
        if (account == null) {
            System.out.println("Account not found!");
            return;
        }

        System.out.print("Select currency: 1 - UAH, 2 - USD, 3 - EUR");
        String sCurrency = sc.nextLine();
        Curses currency = new Curses();
        currency.setName("UAH");
        if (sCurrency.equals("2"))
            currency.setName("USD");
        else if (sCurrency.equals("3"))
            currency.setName("EUR");


        System.out.println("Enter Amount:");
        String sAmount = sc.nextLine();
        int amount = Integer.parseInt(sAmount);
        Double amnt = ammount;

        em.getTransaction().begin();
        try {
            if (!currency.getName().equals(account.getValuta())) {


                if (currency.getName() == "UAH") {
                    ammount = amnt;
                } else if (currency.getName() == "USD") {

                    ammount = amnt * 39;
                } else {
                    ammount = amnt * 41;
                }

                Transactions transaction = new Transactions(ammount, account) {

                };
                em.persist(transaction);
                em.getTransaction().commit();
                System.out.println("Money Added!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return;
        }
    }


    private static void rates_init() {
        em.getTransaction().begin();
        Curses kursUsd = new Curses("USD", 39.1);
        em.persist(kursUsd);
        Curses kursEuro = new Curses("EUR", 41.1);
        em.persist(kursEuro);
        em.getTransaction().commit();
    }

    private static void addUser() {
        em.getTransaction().begin();
        System.out.print("Enter client name: ");
        String name = sc.nextLine();
        Users user = new Users(name);
        em.persist(user);
        em.getTransaction().commit();
        System.out.println("New Client  was Added!");
    }

    private static void addAccount() {
        System.out.print("Enter client name: ");
        String uname = sc.nextLine();

        Query query = em.createQuery("SELECT c FROM Users c WHERE c.name = :name");
        query.setParameter("name", uname);
        try {
            Users user = (Users) query.getSingleResult();
            System.out.print("Select currency: 1 - UAH, 2 - USD, 3 - EUR");
            String sCurrency = sc.nextLine();
            Curses currency = new Curses();
            currency.setName("UAH");
            if (sCurrency.equals("2"))
                currency.setName("USD");
            else if (sCurrency.equals("3"))
                currency.setName("EUR");

            System.out.print("Enter amount: ");
            String amount = sc.nextLine();
            Double amountInt = Double.valueOf(Integer.parseInt(amount));

            em.getTransaction().begin();
            Account account = new Account(user, currency.getName(), amountInt);
            em.persist(account);
            em.getTransaction().commit();
            System.out.println("New Account  was added!");
        } catch (NoResultException ex) {
            System.out.println("Wrong user!");
            return;
        }








}
}
