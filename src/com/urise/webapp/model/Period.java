package com.urise.webapp.model;

import java.util.Objects;

public class Period {
    private final String header;
    private final String date;
    private final String title;
    private final String description;

    public Period(String header, String date, String title, String description) {
        this.header = header;
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public String getHeader() {
        return header;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Period{" +
                "header='" + header + '\'' +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!header.equals(period.header)) return false;
        if (!date.equals(period.date)) return false;
        if (!title.equals(period.title)) return false;
        return Objects.equals(description, period.description);
    }

    @Override
    public int hashCode() {
        int result = header.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
