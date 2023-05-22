package org.example.rpc;

import org.example.rpc.DTO.permission.CheckPermissionRequestDTO;
import org.example.rpc.DTO.permission.CheckPermissionResponseDTO;
import org.example.rpc.DTO.permission.ExpandPermissionTreeRequestDTO;
import org.example.rpc.DTO.permission.ExpandPermissionTreeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "permission",url = "${spicedb.service.url}")
public interface PermissionFeignClient {
    @PostMapping("/v1/permissions/check")
    CheckPermissionResponseDTO checkPermission(@RequestHeader("Authorization") String token, CheckPermissionRequestDTO request);

    @PostMapping("/v1/permissions/expand")
    ExpandPermissionTreeResponseDTO expandPermissionTree(@RequestHeader("Authorization") String token, ExpandPermissionTreeRequestDTO requestDTO);

}
