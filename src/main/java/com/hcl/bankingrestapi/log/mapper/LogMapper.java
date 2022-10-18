package com.hcl.bankingrestapi.log.mapper;

import com.hcl.bankingrestapi.kafka.dto.LogMessage;
import com.hcl.bankingrestapi.log.entity.LogDetail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LogMapper {
    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);

    LogDetail convertToLogDetail(LogMessage logMessage);
}
