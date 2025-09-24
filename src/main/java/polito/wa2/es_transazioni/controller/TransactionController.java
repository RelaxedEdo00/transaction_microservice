package polito.wa2.es_transazioni.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polito.wa2.es_transazioni.DTOs.TransactionDTO;
import polito.wa2.es_transazioni.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name="Transaction Management", description="APIs for managing transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Create a new transaction
    @Operation(
        summary = "Create a new transaction",
        description = "Creates a new transaction with the provided details and returns the created transaction."
    )
    @ApiResponse(responseCode = "201", description = "Transaction created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transaction) {
        logger.info("Creating a new transaction with the provided details"+ transaction.toString());
        TransactionDTO createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.status(201).body(createdTransaction);
    }

    // Get all transactions with pagination, sorting, and filtering
    @Operation(
        summary = "Get all transactions",
        description = "Retrieves a list of all transactions."
    )
    @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        logger.info("Retrieving all transactions from the database");
        List<TransactionDTO> transactions = transactionService.getAllTransactions(page, size, sortBy, order, currency, minAmount, maxAmount, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    // Get transaction by ID
    @Operation(
        summary = "Get transaction by ID",
        description = "Retrieves a transaction by its ID."
    )
    @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long transactionId) {
        TransactionDTO transaction = transactionService.getTransactionById(transactionId);

        return ResponseEntity.ok(transaction);
    }

    // Update transaction by ID
    @Operation(
        summary = "Update a transaction",
        description = "Updates the details of an existing transaction by its ID."
    )
    @ApiResponse(responseCode = "200", description = "Transaction updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long transactionId, @Valid @RequestBody TransactionDTO transactionDetails) {
        TransactionDTO updatedTransaction = transactionService.updateTransaction(transactionId, transactionDetails);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Delete all transactions
    @Operation(
        summary = "Delete all transactions",
        description = "Deletes all transactions from the system."
    )
    @ApiResponse(responseCode = "204", description = "All transactions deleted successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransactions() {
        transactionService.deleteAllTransactions();
        return ResponseEntity.noContent().build();
    }

    // Delete transaction by ID
    @Operation(
        summary = "Delete transaction by ID",
        description = "Deletes a transaction by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Transaction deleted successfully")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransactionById(@PathVariable Long transactionId) {
        transactionService.deleteTransactionById(transactionId);
        return ResponseEntity.noContent().build();
    }
}
