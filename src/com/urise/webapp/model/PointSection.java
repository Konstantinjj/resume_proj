package com.urise.webapp.model;

import java.util.List;

public class PointSection extends Section {
    private final List<String> points;

    public PointSection(List<String> points) {
        this.points = points;
    }

    public List<String> getPoint() {
        return points;
    }

    @Override
    public String toString() {
        return points.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointSection that = (PointSection) o;

        return points.equals(that.points);
    }

    @Override
    public int hashCode() {
        return points.hashCode();
    }
}
