package Supplier.Mgt.Supplier.Mgt.service;

import Supplier.Mgt.Supplier.Mgt.dto.SupplierRequest;
import Supplier.Mgt.Supplier.Mgt.dto.SupplierResponse;

public interface SupplierService {

    SupplierResponse addSupplier(SupplierRequest request);

    SupplierResponse updateSupplier(Long id, SupplierRequest request);

    SupplierResponse getSupplier(Long id);

    void deleteSupplier(Long id);
}
