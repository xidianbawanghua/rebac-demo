package org.example.rpc.DTO.relationship;

import lombok.Data;

@Data
public class SubjectReferenceDTO {
    private ObjectReferenceDTO object;
   private String optionalRelation;
}

