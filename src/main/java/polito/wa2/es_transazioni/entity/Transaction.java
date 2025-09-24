package polito.wa2.es_transazioni.entity;

import jakarta.persistence.Entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Entity
@Table(name = "transactions")
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Double amount;


    private String currency;


    @Column(length = 500)
    private String description;


    private LocalDateTime timestamp;

    private String senderAccount;

    private String receiverAccount;


// constructors, getters, setters


    public Transaction() {}


    public Transaction(Double amount, String currency, String description, LocalDateTime timestamp) {
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.timestamp = timestamp;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getSenderAccount() { return senderAccount; }
    public void setSenderAccount(String senderAccount) { this.senderAccount = senderAccount; }
    public String getReceiverAccount() { return receiverAccount; }
    public void setReceiverAccount(String receiverAccount) { this.receiverAccount = receiverAccount;}
}
