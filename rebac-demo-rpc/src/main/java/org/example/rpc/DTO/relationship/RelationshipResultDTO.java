package org.example.rpc.DTO.relationship;

import lombok.Data;

@Data
public class RelationshipResultDTO {
    private ZedTokenDTO readAt;
    private RelationshipDTO relationship;
//    private CursorDTO afterResultCursor; //无此项
}
