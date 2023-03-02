package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            writeWithException(r.getContacts().entrySet(), dos, contactTypeStringEntry -> {
                dos.writeUTF(contactTypeStringEntry.getKey().name());
                dos.writeUTF(contactTypeStringEntry.getValue());
            });

            writeWithException(r.getSections().entrySet(), dos, entry -> {
                SectionType type = entry.getKey();
                dos.writeUTF(type.name());
                AbstractSection section = entry.getValue();
                switch (type) {
                    case PERSONAL, OBJECTIVE -> writeTextSection(dos, (TextSection) section);
                    case ACHIEVEMENT, QUALIFICATIONS -> writeListSection(dos, (ListSection) section);
                    case EXPERIENCE, EDUCATION -> writeOrganizationSection(dos, (OrganizationSection) section);
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            readWithException(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readWithException(dis, () -> addSection(resume, dis));
            return resume;
        }
    }

    private interface Writer<T> {
        void write(T t) throws IOException;
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            writer.write(item);
        }
    }

    private void writeTextSection(DataOutputStream dos, TextSection textSection) throws IOException {
        dos.writeUTF(textSection.getDescription());
    }

    private void writeListSection(DataOutputStream dos, ListSection listSection) throws IOException {
        writeWithException(listSection.getPoints(), dos, dos::writeUTF);
    }

    private void writeOrganizationSection(DataOutputStream dos, OrganizationSection organizationSection) throws IOException {
        writeWithException(organizationSection.getOrganizations(), dos, organization -> {
            dos.writeUTF(organization.getHeader().getName());
            String url = organization.getHeader().getUrl();
            dos.writeUTF((url != null) ? url : "");
            writeParagraphs(dos, organization.getParagraphs());
        });
    }

    private void writeParagraphs(DataOutputStream dos, List<Paragraph> paragraphs) throws IOException {
        writeWithException(paragraphs, dos, paragraph -> {
            writeLocalDate(dos, paragraph.getStartDate());
            writeLocalDate(dos, paragraph.getEndDate());
            dos.writeUTF(paragraph.getTitle());
            String description = paragraph.getDescription();
            dos.writeUTF((description != null) ? description : "");
        });
    }

    private interface Reader {
        void read() throws IOException;
    }

    private void readWithException(DataInputStream dis, Reader reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }

    private void addSection(Resume r, DataInputStream dis) throws IOException {
        SectionType st = SectionType.valueOf(dis.readUTF());
        switch (st) {
            case PERSONAL, OBJECTIVE -> addTextSection(r, dis, st);
            case ACHIEVEMENT, QUALIFICATIONS -> r.addSection(st, new ListSection(addSection(dis, dis::readUTF)));
            case EXPERIENCE, EDUCATION -> r.addSection(st, new OrganizationSection(
                    addSection(dis, () -> new Organization(new Link(dis.readUTF(), dis.readUTF()),
                            addSection(dis, () -> new Paragraph(readLocalDate(dis), readLocalDate(dis), dis.readUTF(), dis.readUTF()))))
            ));
        }
    }

    private void addTextSection(Resume r, DataInputStream dis, SectionType st) throws IOException {
        r.addSection(st, new TextSection(dis.readUTF()));
    }

    private interface ElementReader<T> {
        T read() throws IOException;
    }

    private <T> List<T> addSection(DataInputStream dis, ElementReader<T> reader) throws IOException {
        List<T> items = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            items.add(reader.read());
        }
        return items;
    }

    private void writeLocalDate(DataOutputStream dos, LocalDate ld) throws IOException {
        dos.writeInt(ld.getYear());
        dos.writeInt(ld.getMonth().getValue());

    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }
}
