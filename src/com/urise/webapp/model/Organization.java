package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;
    private Link header;
    private List<Paragraph> paragraphs;

    public Organization(Link header, List<Paragraph> paragraphs) {
        this.header = header;
        this.paragraphs = paragraphs;
    }

    public Organization(String name, String url, List<Paragraph> paragraphs) {
        this.header = new Link(name, url);
        this.paragraphs = paragraphs;
    }

    public Organization() {
    }

    public Link getHeader() {
        return header;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!header.equals(that.header)) return false;
        return paragraphs.equals(that.paragraphs);
    }

    @Override
    public int hashCode() {
        int result = header.hashCode();
        result = 31 * result + paragraphs.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "header=" + header +
                ", paragraphs=" + paragraphs +
                '}';
    }
}
