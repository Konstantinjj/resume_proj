package com.urise.webapp.model;

import java.util.List;

public class PeriodSection extends Section {
    private final List<Period> periods;

    public PeriodSection(List<Period> periods) {
        this.periods = periods;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PeriodSection that = (PeriodSection) o;

        return periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        return periods.hashCode();
    }

    @Override
    public String toString() {
        return "PeriodSection{" +
                "periods=" + periods +
                '}';
    }
}
