package org.example.rpc.DTO.relationship;

import lombok.Data;

@Data
public class PreconditionDTO {
    private OperationDTO operation;
    private RelationFilterDTO filter;
}
