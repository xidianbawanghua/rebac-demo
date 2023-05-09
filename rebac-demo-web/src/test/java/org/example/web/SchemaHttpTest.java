package org.example.web;

import org.example.rpc.DTO.WriteSchemaRequestDTO;
import org.example.rpc.SchemaFeignClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SchemaHttpTest {

    @Autowired
    private SchemaFeignClient schemaFeignClient;

    @Test
    public void testWriteSchema() {
        WriteSchemaRequestDTO dto = new WriteSchemaRequestDTO();
        String schema = "definition blog/user {}\n" +
                "                            \n" +
                "            definition blog/post {\n" +
                "                relation reader: blog/user\n" +
                "                relation writer: blog/user\n" +
                "                            \n" +
                "                permission read = reader + writer\n" +
                "                permission write = writer\n" +
                "            }";
        dto.setSchema(schema);

        String bearer_lx_token = schemaFeignClient.writeSchema("Bearer lx_token", dto);
        System.out.println(bearer_lx_token);
    }

    @Test
    public void testReadSchema() {
        String s = schemaFeignClient.readSchema("Bearer lx_token");
        System.out.println(s);
    }
}
