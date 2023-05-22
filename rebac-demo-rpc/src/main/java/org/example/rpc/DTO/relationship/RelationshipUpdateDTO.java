package org.example.rpc.DTO.relationship;

import lombok.Data;
import org.example.rpc.OperationEnum;

@Data
public class RelationshipUpdateDTO {
    private OperationEnum operation;
    private RelationshipDTO relationship;
}
