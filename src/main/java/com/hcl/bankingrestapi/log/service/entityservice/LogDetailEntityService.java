package com.hcl.bankingrestapi.log.service.entityservice;

import com.hcl.bankingrestapi.general.service.BaseEntityService;
import com.hcl.bankingrestapi.log.entity.LogDetail;
import com.hcl.bankingrestapi.log.repository.LogDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class LogDetailEntityService extends BaseEntityService<LogDetail, LogDetailsRepository> {
    public LogDetailEntityService(LogDetailsRepository repository) {
        super(repository);
    }
}
