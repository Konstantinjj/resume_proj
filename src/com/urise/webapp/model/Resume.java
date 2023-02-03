package com.urise.webapp.model;

import java.util.*;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    // Unique identifier
    private final String uuid;
    private final String fullName;

    private final Map<ContactType, String> contacts = new HashMap<>();
    private final Map<SectionType, Section> sections = new HashMap<>();

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
//        putInMap();
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
//        putInMap();
    }

    public void addContact(ContactType type, String value) {
        contacts.put(type, value);
    }

    public void addSection(SectionType type, Section section) {
        sections.put(type, section);
    }

    public String getContact(ContactType type) {
        return contacts.get(type);
    }

    public Section getSection(SectionType type) {
        return sections.get(type);
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!uuid.equals(resume.uuid)) return false;
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return uuid + " Fullname: " + fullName;
    }

    @Override
    public int compareTo(Resume o) {
        int comp = fullName.compareTo(o.fullName);
        if (comp == 0) {
            return uuid.compareTo(o.uuid);
        }
        return comp;
    }

//    private void putInMap() {
//        if (contacts.size() == 0) {
//            return;
//        }
//        ;
//        for (ContactType type : ContactType.values()) {
//            contacts.put(type, "");
//        }
////        for (SectionType type : SectionType.values()) {
////            sections.put(type, "");
////        }
//
//    }
}
