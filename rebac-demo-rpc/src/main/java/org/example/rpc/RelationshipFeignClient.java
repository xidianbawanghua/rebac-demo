package org.example.rpc;

import org.example.rpc.DTO.relationship.ReadRelationshipsRequestDTO;
import org.example.rpc.DTO.relationship.ReadRelationshipsResponseDTO;
import org.example.rpc.DTO.relationship.WriteRelationshipsRequestDTO;
import org.example.rpc.DTO.relationship.WriteRelationshipsResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "relationship",url = "${spicedb.service.url}")
public interface RelationshipFeignClient {
    @PostMapping("/v1/relationships/write")
    WriteRelationshipsResponseDTO writeRelationships(@RequestHeader("Authorization") String token, WriteRelationshipsRequestDTO request);

    @PostMapping("/v1/relationships/read")
    String readRelationships(@RequestHeader("Authorization") String token, ReadRelationshipsRequestDTO request);
}
