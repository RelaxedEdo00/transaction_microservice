package polito.wa2.es_transazioni.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polito.wa2.es_transazioni.DTOs.TransactionDTO;
import polito.wa2.es_transazioni.entity.Transaction;
import polito.wa2.es_transazioni.repository.TransactionRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Convert Entity to DTO and vice versa
    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setCurrency(transaction.getCurrency());
        dto.setDescription(transaction.getDescription());
        dto.setTimestamp(transaction.getTimestamp());
        dto.setReceiverAccount(transaction.getReceiverAccount());
        dto.setSenderAccount(transaction.getSenderAccount());
        return dto;
    }

    private Transaction convertToEntity(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setCurrency(dto.getCurrency());
        transaction.setDescription(dto.getDescription());
        transaction.setTimestamp(dto.getTimestamp());
        transaction.setReceiverAccount(dto.getReceiverAccount());
        transaction.setSenderAccount(dto.getSenderAccount());
        return transaction;
    }

    // Create, Read, Update, Delete methods

    // Create
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = convertToEntity(transactionDTO);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }

    // Read all with pagination, sorting, and filtering
    public List<TransactionDTO> getAllTransactions(
            int page, int size, String sortBy, String order,
            String currency, Double minAmount, Double maxAmount,
            String startDate, String endDate) {

        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Transaction> spec = Specification.allOf();

        if (currency != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("currency"), currency));
        }
        if (minAmount != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
        }
        if (maxAmount != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
        }
        if (startDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("timestamp"), OffsetDateTime.parse(startDate)));
        }
        if (endDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("timestamp"), OffsetDateTime.parse(endDate)));
        }

        List<Transaction> transactions = transactionRepository.findAll(spec, pageable).getContent();
        return transactions.stream().map(this::convertToDTO).toList();
    }

    // Read by ID
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction with id " + id + " not found"));
        return convertToDTO(transaction);
    }


    // Update
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDetails) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction with id " + id + " not found"));

        transaction.setAmount(transactionDetails.getAmount());
        transaction.setCurrency(transactionDetails.getCurrency());
        transaction.setDescription(transactionDetails.getDescription());
        transaction.setTimestamp(transactionDetails.getTimestamp());

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToDTO(updatedTransaction);
    }

    // Delete all
    public void deleteAllTransactions() {
        transactionRepository.deleteAll();
    }

    // Delete by ID
    public void deleteTransactionById(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction with id " + id + " not found");
        }
        transactionRepository.deleteById(id);
    }
}
