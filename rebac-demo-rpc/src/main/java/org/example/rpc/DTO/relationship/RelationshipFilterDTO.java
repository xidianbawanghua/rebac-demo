package org.example.rpc.DTO.relationship;

import lombok.Data;

@Data
public class RelationshipFilterDTO {
    private String resourceType;
    private String optionalResourceId;
    private String optionalRelation;
    private SubjectFilterDTO optionalSubjectFilter;
}
