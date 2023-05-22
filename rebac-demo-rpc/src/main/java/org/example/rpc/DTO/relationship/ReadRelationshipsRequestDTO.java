package org.example.rpc.DTO.relationship;

import lombok.Data;

@Data
public class ReadRelationshipsRequestDTO {
    private ConsistencyDTO consistency;
    private RelationshipFilterDTO relationshipFilter;
    private int optionalLimit;
    private CursorDTO optionalCursor;
}
