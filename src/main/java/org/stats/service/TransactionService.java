package org.stats.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stats.dto.Transaction;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private StatsService statsService;

    public void handleTransaction(Transaction transaction) throws Exception {
        logger.debug("Processing transaction, {}", transaction);
        statsService.addInMap(transaction);
    }
}
