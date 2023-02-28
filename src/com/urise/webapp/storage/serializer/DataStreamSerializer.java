package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            Map<SectionType, AbstractSection> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                writeSection(entry, dos);
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                addSection(resume, dis);
            }
            return resume;
        }
    }

    private void writeSection(Map.Entry<SectionType, AbstractSection> entry, DataOutputStream dos) throws IOException {
        SectionType sectionType = entry.getKey();
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> writeTextSection(dos, (TextSection) entry.getValue());
            case ACHIEVEMENT, QUALIFICATIONS -> writeListSection(dos, (ListSection) entry.getValue());
            case EXPERIENCE, EDUCATION -> writeOrganizationSection(dos, (OrganizationSection) entry.getValue());
        }
    }

    private void writeTextSection(DataOutputStream dos, TextSection textSection) throws IOException {
        dos.writeUTF(textSection.getDescription());
    }

    private void writeListSection(DataOutputStream dos, ListSection listSection) throws IOException {
        List<String> list = listSection.getPoints();
        dos.writeInt(list.size());
        for (String s : list) {
            dos.writeUTF(s);
        }
    }

    private void writeOrganizationSection(DataOutputStream dos, OrganizationSection organizationSection) throws IOException {
        List<Organization> organizations = organizationSection.getOrganizations();
        dos.writeInt(organizations.size());

        for (Organization organization : organizations) {
            dos.writeUTF(organization.getHeader().getName());
            String url = organization.getHeader().getUrl();
            dos.writeUTF((url != null) ? url : "");
            List<Paragraph> paragraphs = organization.getParagraphs();
            dos.writeInt(paragraphs.size());
            for (Paragraph paragraph : paragraphs) {
                writeLocalDate(dos, paragraph.getStartDate());
                writeLocalDate(dos, paragraph.getEndDate());
                dos.writeUTF(paragraph.getTitle());
                String description = paragraph.getDescription();
                dos.writeUTF((description != null) ? description : "");
            }
        }
    }

    private void addSection(Resume r, DataInputStream dis) throws IOException {
        SectionType st = SectionType.valueOf(dis.readUTF());
        switch (st) {
            case PERSONAL, OBJECTIVE -> addTextSection(r, dis, st);
            case ACHIEVEMENT, QUALIFICATIONS -> addListSection(r, dis, st);
            case EXPERIENCE, EDUCATION -> addOrganizationSection(r, dis, st);
        }
    }

    private void addTextSection(Resume r, DataInputStream dis, SectionType st) throws IOException {
        r.addSection(st, new TextSection(dis.readUTF()));
    }

    private void addListSection(Resume r, DataInputStream dis, SectionType st) throws IOException {
        List<String> items = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            items.add(dis.readUTF());
        }
        r.addSection(st, new ListSection(items));
    }

    private void addOrganizationSection(Resume r, DataInputStream dis, SectionType st) throws IOException {
        List<Organization> organizations = new ArrayList<>();
        int size = dis.readInt();
        Link link;
        for (int i = 0; i < size; i++) {

//            link = new Link(dis.readUTF(), dis.readUTF());
            String name = dis.readUTF();
            String url = dis.readUTF();
            if (url.equals("")) {
                link = new Link(name, null);
            } else {
                link = new Link(name, url);
            }

            int parSize = dis.readInt();
            List<Paragraph> paragraphs = new ArrayList<>();
            for (int j = 0; j < parSize; j++) {
                LocalDate startDate = readLocalDate(dis);
                LocalDate endDate = readLocalDate(dis);
                String title = dis.readUTF();
                String description = dis.readUTF();
                if (description.equals("")) description = null;
                paragraphs.add(new Paragraph(startDate, endDate, title, description));
            }
            organizations.add(new Organization(link, paragraphs));
        }
        r.addSection(st, new OrganizationSection(organizations));
    }

    private void writeLocalDate(DataOutputStream dos, LocalDate ld) throws IOException {
        dos.writeInt(ld.getYear());
        dos.writeInt(ld.getMonth().getValue());
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }
}
