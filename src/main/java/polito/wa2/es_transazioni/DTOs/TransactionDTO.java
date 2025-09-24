package polito.wa2.es_transazioni.DTOs;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private Double amount;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid ISO 4217 code")
    private String currency;

    @NotBlank(message = "Sender account is required")
    @Pattern(
            regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{10,30}$",
            message = "Sender account must be a valid IBAN-like format"
    )
    private String senderAccount;

    @NotBlank(message = "Receiver account is required")
    @Pattern(
            regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{10,30}$",
            message = "Receiver account must be a valid IBAN-like format"
    )
    private String receiverAccount;

    @NotNull(message = "Timestamp is required")
    @PastOrPresent(message = "Timestamp cannot be in the future")
    private LocalDateTime timestamp;

    @NotNull(message = "Description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
