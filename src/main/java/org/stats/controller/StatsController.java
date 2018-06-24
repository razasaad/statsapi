package org.stats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stats.dto.DoubleSummaryStatisticsCustom;
import org.stats.service.StatsService;

@RestController
@RequestMapping("/statistics")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    public DoubleSummaryStatisticsCustom doubleSummaryStatistics() {
        return statsService.computeStatistics();
    }
}
