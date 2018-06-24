package org.stats.utils;

import org.stats.dto.Transaction;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TestUtils {

    private static long getCurrentTimestamp() {
        Instant instant = Instant.now();
        return instant.toEpochMilli();
    }

    public static Transaction generateRandomTransaction(boolean within60Seconds) {
        int seconds;
        Random random = new Random();

        Transaction transaction = new Transaction();
        transaction.setAmount(random.nextDouble());

        if (within60Seconds) {
            seconds = random.nextInt(60);
        } else {
            seconds = random.nextInt(60) + 60;
        }

        transaction.setTimestamp(TestUtils.getCurrentTimestamp() - (TimeUnit.SECONDS.toMillis(seconds)));

        return transaction;
    }

    public static Transaction generateRandomTransaction(int exactSecondsBeforeTransacted) {
        Random random = new Random();

        Transaction transaction = new Transaction();
        transaction.setAmount(random.nextDouble());

        transaction.setTimestamp(TestUtils.getCurrentTimestamp() -
                (TimeUnit.SECONDS.toMillis(exactSecondsBeforeTransacted)));

        return transaction;
    }

    public static DoubleSummaryStatistics generateStatisticSummary(List<Transaction> transactions) {

        return transactions
                .stream()
                .collect(Collectors.summarizingDouble(Transaction::getAmount));
    }
}
