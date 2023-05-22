package org.example.rpc.DTO.permission;

import lombok.Data;
import org.example.rpc.DTO.relationship.ObjectReferenceDTO;

@Data
public class PermissionRelationshipTreeDTO {
    //---one of
    private AlgebraicSubjectSetDTO intermediate;
    private DirectSubjectSetDTO leaf;
    //---
    private ObjectReferenceDTO expandedObject;
    private String expandedRelation;
}
