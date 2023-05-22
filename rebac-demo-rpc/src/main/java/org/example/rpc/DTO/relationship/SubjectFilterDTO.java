package org.example.rpc.DTO.relationship;

import lombok.Data;

@Data
public class SubjectFilterDTO {
    private String subjectType;
    private String optionalSubjectId;
    private RelationFilterDTO optionalRelation;
}
