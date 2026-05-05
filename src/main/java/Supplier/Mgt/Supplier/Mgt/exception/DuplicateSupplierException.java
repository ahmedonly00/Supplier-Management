package Supplier.Mgt.Supplier.Mgt.exception;

public class DuplicateSupplierException extends RuntimeException {

    public DuplicateSupplierException(String email) {
        super("Supplier with email '" + email + "' already exists");
    }
}
