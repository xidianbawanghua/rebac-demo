package org.example.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.rpc.DTO.schema.ReadSchemaResponseDTO;
import org.example.rpc.DTO.schema.WriteSchemaRequestDTO;
import org.example.rpc.SchemaFeignClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SchemaTest {

    @Autowired
    private SchemaFeignClient schemaFeignClient;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testWriteSchema() {
        WriteSchemaRequestDTO dto = new WriteSchemaRequestDTO();
        String schema = "definition blog/user {}\n" +
                "\n" +
                "definition user {}\n" +
                "\n" +
                "definition role {\n" +
                "    relation member: user\n" +
                "}\n" +
                "\n" +
                "definition blog/post {\n" +
                "    relation reader: blog/user\n" +
                "    relation writer: blog/user\n" +
                "    permission read = reader + writer\n" +
                "    permission write = writer\n" +
                "}\n" +
                "\n" +
                "definition resource {\n" +
                "    relation reader: role#member\n" +
                "    permission read = reader\n" +
                "}\n" +
                "\n" +
                "definition n_1684247746470_user {}\n" +
                " definition namespace_1684247917619_user {} \n"
                +
                "definition namespace_1684247917619_role { \n" +
                "  relation member:user\n" +
                "} "
                ;
        dto.setSchema(schema);

        schemaFeignClient.writeSchema("Bearer lx_token", dto);

    }

    @Test
    public void testReadSchema() throws JsonProcessingException {
        ReadSchemaResponseDTO responseDTO = schemaFeignClient.readSchema("Bearer lx_token");
        Assert.assertNotNull(responseDTO.getSchemaText());
        System.out.println(responseDTO.getSchemaText());
    }


}
