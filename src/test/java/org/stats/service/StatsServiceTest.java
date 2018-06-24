package org.stats.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.stats.dto.DoubleSummaryStatisticsCustom;
import org.stats.dto.Transaction;
import org.stats.exception.StaleTransactionException;
import org.stats.utils.TestUtils;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatsServiceTest {

    @Autowired
    private StatsService statsService;

    private static final double DELTA = 0.001;

    @Before
    public void setupMap() {
        statsService.initializeMap();
    }

    @Test(expected = StaleTransactionException.class)
    public void testInvalidEntry() throws Exception {

        statsService.addInMap(TestUtils.generateRandomTransaction(false));

    }

    @Test
    public void testValidEntry() throws Exception {

        statsService.addInMap(TestUtils.generateRandomTransaction(true));

        assertEquals(1, statsService.mapSize());
    }

    @Test
    public void testDoubleStatisticsSummary() throws Exception {

        List<Transaction> transactionList = new ArrayList<>();

        Transaction firstTransaction = TestUtils.generateRandomTransaction(true);
        Transaction secondTransaction = TestUtils.generateRandomTransaction(true);
        Transaction thirdTransaction = TestUtils.generateRandomTransaction(true);

        transactionList.add(firstTransaction);
        transactionList.add(secondTransaction);
        transactionList.add(thirdTransaction);

        for (Transaction transaction : transactionList) {
            statsService.addInMap(transaction);
        }

        DoubleSummaryStatisticsCustom actualStatistics = statsService.computeStatistics();

        DoubleSummaryStatistics expectedStatistics = TestUtils.generateStatisticSummary(transactionList);

        assertEquals(expectedStatistics.getCount(), actualStatistics.getCount());
        assertEquals(expectedStatistics.getAverage(), actualStatistics.getAverage(), DELTA);
        assertEquals(expectedStatistics.getMax(), actualStatistics.getMax(), DELTA);
        assertEquals(expectedStatistics.getMin(), actualStatistics.getMin(), DELTA);
        assertEquals(expectedStatistics.getSum(), actualStatistics.getSum(), DELTA);

    }

    @Test
    public void testDoubleStatisticsSummaryWithEvictedKey() throws Exception {

        List<Transaction> transactionList = new ArrayList<>();

        Transaction firstTransaction = TestUtils.generateRandomTransaction(30);
        Transaction secondTransaction = TestUtils.generateRandomTransaction(30);
        Transaction thirdTransaction = TestUtils.generateRandomTransaction(20);
        Transaction fourthTransaction = TestUtils.generateRandomTransaction(58);


        transactionList.add(firstTransaction);
        transactionList.add(secondTransaction);
        transactionList.add(thirdTransaction);

        for (Transaction transaction : transactionList) {
            statsService.addInMap(transaction);
        }
        statsService.addInMap(fourthTransaction);

        Thread.sleep(3000);

        DoubleSummaryStatisticsCustom actualStatistics = statsService.computeStatistics();

        DoubleSummaryStatistics expectedStatistics = TestUtils.generateStatisticSummary(transactionList);

        assertEquals(expectedStatistics.getCount(), actualStatistics.getCount());
        assertEquals(expectedStatistics.getAverage(), actualStatistics.getAverage(), DELTA);
        assertEquals(expectedStatistics.getMax(), actualStatistics.getMax(), DELTA);
        assertEquals(expectedStatistics.getMin(), actualStatistics.getMin(), DELTA);
        assertEquals(expectedStatistics.getSum(), actualStatistics.getSum(), DELTA);

    }

}
