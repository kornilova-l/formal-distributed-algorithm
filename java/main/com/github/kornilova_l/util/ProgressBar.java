package com.github.kornilova_l.util;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class ProgressBar {
    private final long startTime = System.currentTimeMillis();
    private int lastCurrent = 0;
    private final int total;
    private final Timer timer;

    public ProgressBar(int total) {
        this.total = total;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                printProgress(lastCurrent);
            }
        }, 0, 1000);
    }

    public void finish() {
        timer.cancel();
        printProgress(total);
    }

    public synchronized void printProgress(int current) {
        lastCurrent = current;
        int percent = (current * 100) / total;
        percent /= 2;
        int resizingTotal = 50;
        StringBuilder string = new StringBuilder(140);
        percent = percent == 0 ? 1 : percent;
        DecimalFormat format = new DecimalFormat("####");
        string.append('\r')
                .append(String.join("", Collections.nCopies(2 - (int) (Math.log10(percent * 2)), " ")))
                .append(String.format(" %d%% [", percent * 2))
                .append(String.join("", Collections.nCopies(percent, "=")))
                .append('>')
                .append(String.join("", Collections.nCopies(resizingTotal - percent, " ")))
                .append(']')
                .append(String.format("%4s", format.format((double) (System.currentTimeMillis() - startTime) / 1000)))
                .append("s");

        System.out.print(string);
    }
}
