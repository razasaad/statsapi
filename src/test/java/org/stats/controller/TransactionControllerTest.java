package org.stats.controller;

import net.minidev.json.JSONValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.stats.dto.Transaction;
import org.stats.utils.TestUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testTransactionIsValid() throws Exception {
        Transaction transaction = TestUtils.generateRandomTransaction(true);

        mvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONValue.toJSONString(transaction)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testTransactionIsStale() throws Exception {
        Transaction transaction = TestUtils.generateRandomTransaction(false);

        mvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONValue.toJSONString(transaction)))
                .andExpect(status().isNoContent());
    }

}
