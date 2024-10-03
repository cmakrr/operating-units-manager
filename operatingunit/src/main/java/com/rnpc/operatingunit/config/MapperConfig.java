package com.rnpc.operatingunit.config;

import com.rnpc.operatingunit.dto.response.operation.OperationPlanFullInfoResponse;
import com.rnpc.operatingunit.model.Operation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        configOperationPlanFullInfoResponse(modelMapper);

        return modelMapper;
    }

    private void configOperationPlanFullInfoResponse(ModelMapper modelMapper) {
        modelMapper.createTypeMap(Operation.class, OperationPlanFullInfoResponse.class)
                .addMapping(Operation::getId, OperationPlanFullInfoResponse::setOperationId)
                .addMapping(src -> src.getOperationPlan().getId(), OperationPlanFullInfoResponse::setId)
                .addMapping(src -> src.getOperationPlan().getStartTime(), OperationPlanFullInfoResponse::setStartTime)
                .addMapping(src -> src.getOperationPlan().getEndTime(), OperationPlanFullInfoResponse::setEndTime)
                .addMapping(src -> src.getOperationPlan().getEndTime(), OperationPlanFullInfoResponse::setEndTime)
                .addMapping(src -> src.getOperationPlan().getOperator(), OperationPlanFullInfoResponse::setOperator)
                .addMapping(src -> src.getOperationPlan().getAssistant(), OperationPlanFullInfoResponse::setAssistant)
                .addMapping(src -> src.getOperationPlan().getTransfusiologist(),
                        OperationPlanFullInfoResponse::setTransfusiologist);
    }
}
