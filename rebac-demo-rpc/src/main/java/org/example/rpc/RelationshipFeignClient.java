package org.example.rpc;

import com.authzed.api.v1.PermissionService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "relationship",url = "${spicedb.service.url}")
public interface RelationshipFeignClient {
//    @PostMapping("/v1/relationships/write")
//    String writeRelationships(@RequestHeader("Authorization") String token) {
//        PermissionService.WriteRelationshipsRequest
//    }
}
