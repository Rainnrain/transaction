package com.example.awardingPoints.util;

public class RewardsCalculator {
    public static int calculatePoints(double transactionAmount) {
        int points = 0;

        if (transactionAmount >= 100) {
            points += (int) ((transactionAmount - 100) * 2);
            points += 50; // bonus points for spending $100 or more
        } else if (transactionAmount >= 50) {
            points += (int) (transactionAmount - 50); // 1 point per dollar from $50-$99.99
        }

        return points;
    }
}
