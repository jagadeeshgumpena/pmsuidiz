package com.dizitiveit.pms.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dizitiveit.pms.model.InvoiceItems;

public interface InvoiceItemsDao extends JpaRepository<InvoiceItems, Long> {
 InvoiceItems findByInvoiceInvoiceId(long invoiceId);

}
