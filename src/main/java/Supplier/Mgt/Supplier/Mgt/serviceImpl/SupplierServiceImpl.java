package Supplier.Mgt.Supplier.Mgt.serviceImpl;

import Supplier.Mgt.Supplier.Mgt.dto.SupplierRequest;
import Supplier.Mgt.Supplier.Mgt.dto.SupplierResponse;
import Supplier.Mgt.Supplier.Mgt.entity.Supplier;
import Supplier.Mgt.Supplier.Mgt.event.SupplierEvent;
import Supplier.Mgt.Supplier.Mgt.exception.DuplicateSupplierException;
import Supplier.Mgt.Supplier.Mgt.exception.SupplierNotFoundException;
import Supplier.Mgt.Supplier.Mgt.kafka.SupplierKafkaProducer;
import Supplier.Mgt.Supplier.Mgt.repository.SupplierRepository;
import Supplier.Mgt.Supplier.Mgt.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

        private final SupplierRepository supplierRepository;
        private final SupplierKafkaProducer kafkaProducer;

        @Override
        @Transactional
        public SupplierResponse addSupplier(SupplierRequest request) {
                log.info("Adding new supplier with email: {}", request.getEmail());

                if (supplierRepository.existsByEmail(request.getEmail())) {
                        throw new DuplicateSupplierException(request.getEmail());
                }

                Supplier supplier = Supplier.builder()
                                .name(request.getName())
                                .email(request.getEmail())
                                .phone(request.getPhone())
                                .address(request.getAddress())
                                .contactPerson(request.getContactPerson())
                                .build();

                Supplier saved = supplierRepository.save(supplier);
                SupplierResponse response = toResponse(saved);

                kafkaProducer.publishEvent(SupplierEvent.builder()
                                .eventType("SUPPLIER_CREATED")
                                .supplierId(saved.getId())
                                .supplierName(saved.getName())
                                .timestamp(LocalDateTime.now())
                                .data(response)
                                .build());

                log.info("Supplier created with id: {}", saved.getId());
                return response;
        }

        @Override
        @Transactional
        public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
                log.info("Updating supplier with id: {}", id);

                Supplier supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> new SupplierNotFoundException(id));

                if (supplierRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                        throw new DuplicateSupplierException(request.getEmail());
                }

                supplier.setName(request.getName());
                supplier.setEmail(request.getEmail());
                supplier.setPhone(request.getPhone());
                supplier.setAddress(request.getAddress());
                supplier.setContactPerson(request.getContactPerson());
                supplier.setUpdatedAt(LocalDateTime.now());

                Supplier updated = supplierRepository.save(supplier);
                SupplierResponse response = toResponse(updated);

                kafkaProducer.publishEvent(SupplierEvent.builder()
                                .eventType("SUPPLIER_UPDATED")
                                .supplierId(updated.getId())
                                .supplierName(updated.getName())
                                .timestamp(LocalDateTime.now())
                                .data(response)
                                .build());

                log.info("Supplier updated with id: {}", updated.getId());
                return response;
        }

        @Override
        @Transactional(readOnly = true)
        public SupplierResponse getSupplier(Long id) {
                log.info("Retrieving supplier with id: {}", id);

                Supplier supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> new SupplierNotFoundException(id));

                SupplierResponse response = toResponse(supplier);

                kafkaProducer.publishEvent(SupplierEvent.builder()
                                .eventType("SUPPLIER_RETRIEVED")
                                .supplierId(supplier.getId())
                                .supplierName(supplier.getName())
                                .timestamp(LocalDateTime.now())
                                .data(response)
                                .build());

                return response;
        }

        @Override
        @Transactional
        public void deleteSupplier(Long id) {
                log.info("Deleting supplier with id: {}", id);

                Supplier supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> new SupplierNotFoundException(id));

                supplierRepository.delete(supplier);

                kafkaProducer.publishEvent(SupplierEvent.builder()
                                .eventType("SUPPLIER_DELETED")
                                .supplierId(supplier.getId())
                                .supplierName(supplier.getName())
                                .timestamp(LocalDateTime.now())
                                .data(Map.of("id", id, "deletedAt", LocalDateTime.now().toString()))
                                .build());

                log.info("Supplier deleted with id: {}", id);
        }

        private SupplierResponse toResponse(Supplier supplier) {
                return SupplierResponse.builder()
                                .id(supplier.getId())
                                .name(supplier.getName())
                                .email(supplier.getEmail())
                                .phone(supplier.getPhone())
                                .address(supplier.getAddress())
                                .contactPerson(supplier.getContactPerson())
                                .status(supplier.getStatus().name())
                                .createdAt(supplier.getCreatedAt())
                                .updatedAt(supplier.getUpdatedAt())
                                .build();
        }
}
