package com.urise.webapp;

import com.urise.webapp.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("TestResume");

        addContact(resume);
        addListSections(resume);
        addOrganizationSections(resume);

        for (ContactType type : ContactType.values()) {
            System.out.println(type.getTitle() + resume.getContact(type));
        }
        System.out.println();

        for (SectionType type : SectionType.values()) {
            System.out.println("\n" + type.getTitle() + "\n" + resume.getSection(type));
        }

    }

    private static void addOrganizationSections(Resume resume) {
        List<Organization> experienceSections = new ArrayList<>();
        List<Organization> educationSections = new ArrayList<>();

        LocalDate startDate = LocalDate.of(1997, 9, 1);
        LocalDate endDate = LocalDate.of(1998, 3, 1);
        experienceSections.add(new Organization("Alcatel", startDate, endDate, "TestExp1", "Descr1"));

        startDate = LocalDate.of(2008, 6, 1);
        endDate = LocalDate.of(2010, 12, 1);
        experienceSections.add(new Organization("Yota", startDate, endDate, "TestExp2", "Descr2"));

        startDate = LocalDate.of(2014, 10, 1);
        endDate = LocalDate.of(2016, 1, 1);
        experienceSections.add(new Organization("Wrike", startDate, endDate, "TestExp3", "Descr3"));

        startDate = LocalDate.of(2011, 3, 1);
        endDate = LocalDate.of(2011, 4, 1);
        educationSections.add(new Organization("Luxoft", startDate, endDate, "TestEduc1"));

        startDate = LocalDate.of(2013, 3, 1);
        endDate = LocalDate.of(2013, 5, 1);
        educationSections.add(new Organization("Coursera", startDate, endDate, "TestEduc2"));

        resume.addSection(SectionType.EXPERIENCE, new OrganizationSection(experienceSections));
        resume.addSection(SectionType.EDUCATION, new OrganizationSection(educationSections));

    }

    private static void addListSections(Resume resume) {
        List<String> personal = new ArrayList<>();
        List<String> objective = new ArrayList<>();
        List<String> achievements = new ArrayList<>();
        List<String> qualifications = new ArrayList<>();

        personal.add("personal_one");
        objective.add("objective_one");
        objective.add("objective_two");

        achievements.add("achievement_one");
        achievements.add("achievement_two");
        achievements.add("achievement_three");

        qualifications.add("qualification_one");
        qualifications.add("qualification_two");
        qualifications.add("qualification_three");
        qualifications.add("qualification_four");

        resume.addSection(SectionType.PERSONAL, new ListSection(personal));
        resume.addSection(SectionType.OBJECTIVE, new ListSection(objective));
        resume.addSection(SectionType.ACHIEVEMENT, new ListSection(achievements));
        resume.addSection(SectionType.QUALIFICATIONS, new ListSection(qualifications));
    }

    private static void addContact(Resume resume) {
        resume.addContact(ContactType.PHONE_NUMBER, String.valueOf(new TextSection("+7(921) 855-0482")));
        resume.addContact(ContactType.SKYPE, String.valueOf(new TextSection("skype:grigory.kislin")));
        resume.addContact(ContactType.MAIL, String.valueOf(new TextSection("gkislin@yandex.ru")));
        resume.addContact(ContactType.LINKEDIN, String.valueOf(new TextSection("URL LinkedIn")));
        resume.addContact(ContactType.GITHUB, String.valueOf(new TextSection("URL GitHub")));
        resume.addContact(ContactType.STACKOVERFLOW, String.valueOf(new TextSection(" URL Профиль Stackoverflow")));
        resume.addContact(ContactType.HOME_PAGE, String.valueOf(new TextSection("URL Домашняя страница")));
    }
}
