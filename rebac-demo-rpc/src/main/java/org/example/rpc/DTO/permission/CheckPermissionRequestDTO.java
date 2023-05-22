package org.example.rpc.DTO.permission;

import lombok.Data;
import org.example.rpc.DTO.relationship.ConsistencyDTO;
import org.example.rpc.DTO.relationship.ObjectReferenceDTO;
import org.example.rpc.DTO.relationship.StructDTO;
import org.example.rpc.DTO.relationship.SubjectReferenceDTO;

@Data
public class CheckPermissionRequestDTO {
    private ConsistencyDTO consistency;
    private ObjectReferenceDTO resource;
    private String permission;
    private SubjectReferenceDTO subject;
    private StructDTO context;
}
