package org.stats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.stats.dto.DoubleSummaryStatisticsCustom;
import org.stats.dto.Transaction;
import org.stats.service.StatsService;
import org.stats.utils.TestUtils;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StatsService statsService;

    @Before
    public void setupMap() {
        statsService.initializeMap();
    }

    @Test
    public void testTransactionSummaryIsCorrect() throws Exception {
        List<Transaction> transactionList = Arrays.asList(
                TestUtils.generateRandomTransaction(30),
                TestUtils.generateRandomTransaction(35));

        mvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONValue.toJSONString(transactionList.get(0))));

        mvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONValue.toJSONString(transactionList.get(1))));

        DoubleSummaryStatistics statistics = TestUtils.generateStatisticSummary(transactionList);
        DoubleSummaryStatisticsCustom custom = DoubleSummaryStatisticsCustom.copyFromDoubleStatisticSummary(statistics);

        String summary = new ObjectMapper().writeValueAsString(custom);

        Thread.sleep(2000);

        mvc.perform(MockMvcRequestBuilders
                .get("/statistics")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(summary)));
    }

    /*
    This might be contradictory but to keep the API uniform,
    it should return empty values instead of throwing an error towards Client.
     */
    @Test
    public void testTransactionsEmptyButSummaryIsReturned() throws Exception {

        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();
        DoubleSummaryStatisticsCustom custom = DoubleSummaryStatisticsCustom.copyFromDoubleStatisticSummary(statistics);

        String summary = new ObjectMapper().writeValueAsString(custom);

        mvc.perform(MockMvcRequestBuilders
                .get("/statistics")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(summary)));
    }

}
