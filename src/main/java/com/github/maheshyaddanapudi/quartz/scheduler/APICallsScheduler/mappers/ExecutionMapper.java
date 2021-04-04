package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.mappers;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsExecHist;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.buffer.ExecutionDetailsBuffer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExecutionMapper {
    ExecutionDetailsBuffer mapExecutionToDto(AcqsExecHist execution);
}
