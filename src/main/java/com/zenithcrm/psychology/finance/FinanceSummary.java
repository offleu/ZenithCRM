package com.zenithcrm.psychology.finance;

import java.math.BigDecimal;

public record FinanceSummary(
        BigDecimal received,
        BigDecimal pending,
        BigDecimal overdue,
        long paidCount,
        long pendingCount,
        long overdueCount
) {
}
