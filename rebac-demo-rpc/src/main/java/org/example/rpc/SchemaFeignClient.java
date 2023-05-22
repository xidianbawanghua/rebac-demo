package org.example.rpc;

import org.example.rpc.DTO.schema.ReadSchemaResponseDTO;
import org.example.rpc.DTO.schema.WriteSchemaRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "schema",url = "${spicedb.service.url}")
public interface SchemaFeignClient {

    @PostMapping("/v1/schema/write")
    void writeSchema(@RequestHeader("Authorization") String token, WriteSchemaRequestDTO dto);

    @PostMapping("/v1/schema/read")
    ReadSchemaResponseDTO readSchema(@RequestHeader("Authorization") String token);
}
