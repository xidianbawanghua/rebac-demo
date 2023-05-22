package org.example.rpc.DTO.relationship;

import lombok.Data;

import java.util.List;

@Data
public class WriteRelationshipsRequestDTO {
    private List<RelationshipUpdateDTO> updates;
    private PreconditionDTO optionalPreconditions;
}
