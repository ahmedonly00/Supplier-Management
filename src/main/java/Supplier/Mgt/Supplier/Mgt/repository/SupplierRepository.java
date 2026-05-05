package Supplier.Mgt.Supplier.Mgt.repository;

import Supplier.Mgt.Supplier.Mgt.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Supplier> findByEmail(String email);
}
