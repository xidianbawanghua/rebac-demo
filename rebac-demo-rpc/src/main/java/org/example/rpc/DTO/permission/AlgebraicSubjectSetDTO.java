package org.example.rpc.DTO.permission;

import lombok.Data;
import org.example.rpc.AlgebraicSubjectSetOperationEnum;

import java.util.List;

@Data
public class AlgebraicSubjectSetDTO {
    private AlgebraicSubjectSetOperationEnum operation;

    //repeated todo
    private List<PermissionRelationshipTreeDTO> children;
}
