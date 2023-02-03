package com.urise.webapp;

import com.urise.webapp.model.*;

import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("TestResume");

        resume.addContact(ContactType.PHONE_NUMBER, "+7(921) 855-0482");
        resume.addContact(ContactType.SKYPE, "skype:grigory.kislin");
        resume.addContact(ContactType.MAIL, "gkislin@yandex.ru");
        resume.addContact(ContactType.LINKEDIN, "Профиль LinkedIn");
        resume.addContact(ContactType.GITHUB, "Профиль GitHub");
        resume.addContact(ContactType.STACKOVERFLOW, "Профиль Stackoverflow");
        resume.addContact(ContactType.HOME_PAGE, "Домашняя страница");

        for (ContactType type : ContactType.values()) {
            System.out.println(type.getTitle() + resume.getContact(type));
        }
        System.out.println();

        List<String> personal = new ArrayList<>();
        personal.add("personal_one");

        List<String> objective = new ArrayList<>();
        objective.add("objective_one");
        objective.add("objective_two");

        List<String> achievements = new ArrayList<>();
        achievements.add("achievement_one");
        achievements.add("achievement_two");
        achievements.add("achievement_three");

        List<String> qualifications = new ArrayList<>();
        qualifications.add("qualification_one");
        qualifications.add("qualification_two");
        qualifications.add("qualification_three");
        qualifications.add("qualification_four");

        resume.addSection(SectionType.PERSONAL, new PointSection(personal));
        resume.addSection(SectionType.OBJECTIVE, new PointSection(objective));
        resume.addSection(SectionType.ACHIEVEMENT, new PointSection(achievements));
        resume.addSection(SectionType.QUALIFICATIONS, new PointSection(qualifications));

        for (SectionType type : SectionType.values()) {
            System.out.println(type.getTitle() + "\n" + resume.getSection(type));
        }

//        List<Section> education = new ArrayList<Section>();
//        education.add("123");
//        education.add("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");
//        resume.addSection(SectionType.EDUCATION, education);
//        resume.addSection(SectionType.EDUCATION);

    }
}
