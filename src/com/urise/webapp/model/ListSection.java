package com.urise.webapp.model;

import java.util.List;

public class ListSection extends AbstractSection {
    private static final long serialVersionUID = 1L;
    private List<String> points;

    public ListSection() {
    }

    public ListSection(List<String> points) {
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

        ListSection that = (ListSection) o;

        return points.equals(that.points);
    }

    @Override
    public int hashCode() {
        return points.hashCode();
    }
}
