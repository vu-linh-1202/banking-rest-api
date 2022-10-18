package com.hcl.bankingrestapi.log.repository;

import com.hcl.bankingrestapi.log.entity.LogDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDetailsRepository extends JpaRepository<LogDetail, Long> {
}
