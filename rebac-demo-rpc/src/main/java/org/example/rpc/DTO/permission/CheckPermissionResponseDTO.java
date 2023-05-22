package org.example.rpc.DTO.permission;

import lombok.Data;
import org.example.rpc.DTO.relationship.ZedTokenDTO;
import org.example.rpc.PermissionshipEnum;

@Data
public class CheckPermissionResponseDTO {
    private ZedTokenDTO checkedAt;
    private PermissionshipEnum permissionship;
    private PartialCaveatInfoDTO partialCaveatInfo;
}
