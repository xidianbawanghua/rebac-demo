package org.example.rpc.DTO.relationship;

import lombok.Data;

@Data
public class RelationshipDTO {
    private ObjectReferenceDTO resource;
    private String relation;
    private SubjectReferenceDTO subject;
    private ContextualizedCaveatDTO optionalCaveat;
}
