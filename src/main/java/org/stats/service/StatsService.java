package org.stats.service;

import com.stoyanr.evictor.ConcurrentMapWithTimedEviction;
import com.stoyanr.evictor.map.ConcurrentHashMapWithTimedEviction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.stats.dto.DoubleSummaryStatisticsCustom;
import org.stats.dto.Transaction;
import org.stats.exception.StaleTransactionException;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * In reality, the MAX_BUCKETS would need not to be decided upon,
 * because at max, their will never
 * be more than 60 buckets(consecutive timestamps  -> 1second apart each).
 * As the keys will start to evict just like a Redis TTL for key,
 * the size will never grow beyond 60 making it a O(1) memory compliant.
 */

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class StatsService {

    private static final Logger logger = LoggerFactory.getLogger(StatsService.class);

    private static final int MAX_BUCKETS = 60;
    private static final long MAX_LOOKBACK_IN_SECONDS = 60;

    private ConcurrentMapWithTimedEviction<Long, DoubleSummaryStatistics> transactionAmountsPerTimestamp;

    public void addInMap(Transaction transaction) throws Exception {
        Optional<Long> optional = calculateDiffInTime(transaction.getTimestamp(), MAX_LOOKBACK_IN_SECONDS);
        if (optional.isPresent()) {
            logger.info("Adding a transaction with timestamp: {}", transaction.getTimestamp());
            long timeToEvict = optional.get();

            if (transactionAmountsPerTimestamp == null) {
                this.initializeMap();
            }

            transactionAmountsPerTimestamp.
                    putIfAbsent(transaction.getTimestamp(), new DoubleSummaryStatistics(), timeToEvict);
            transactionAmountsPerTimestamp.get(transaction.getTimestamp()).accept(transaction.getAmount());
        } else {
            throw new StaleTransactionException();
        }
    }

    private Optional<Long> calculateDiffInTime(long timestamp, long timeRangeInSeconds) {
        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli();

        Optional<Long> optional = Optional.empty();
        long diff = timeStampMillis - timestamp;
        if (TimeUnit.MILLISECONDS.toSeconds(diff) <= timeRangeInSeconds) {
            long timeToEvict = (TimeUnit.SECONDS.toMillis(timeRangeInSeconds)) - diff;
            optional = Optional.of(timeToEvict);
        }
        return optional;
    }

    public DoubleSummaryStatisticsCustom computeStatistics() {
        List<DoubleSummaryStatistics> summaryPerTimestamp;
        DoubleSummaryStatistics doubleSummaryStatistics = new DoubleSummaryStatistics();

        try {
            summaryPerTimestamp = transactionAmountsPerTimestamp.values().stream().collect(Collectors.toList());
            for (DoubleSummaryStatistics stats : summaryPerTimestamp) {
                doubleSummaryStatistics.combine(stats);
            }
        } catch (NullPointerException e) {
            logger.info("No records found for any timestamp!");
        }

        return DoubleSummaryStatisticsCustom.
                copyFromDoubleStatisticSummary(doubleSummaryStatistics);
    }

    /*
    Only Exposed for Tests. Otherwise TestSuite run causes issues due to
    map being populated by all tests.
     */
    public void initializeMap() {
        transactionAmountsPerTimestamp = new ConcurrentHashMapWithTimedEviction<>(MAX_BUCKETS);
    }

    public int mapSize() {
        return transactionAmountsPerTimestamp.size();
    }


}
