package com.urise.webapp.model;

public class TextSection extends Section {
    private final String description;

    public TextSection(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "TextSection{" +
                "description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextSection that = (TextSection) o;

        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
