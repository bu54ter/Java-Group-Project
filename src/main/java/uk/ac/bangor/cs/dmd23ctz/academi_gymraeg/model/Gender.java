package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

/**
 * Enumeration representing grammatical gender.
 *
 * <p>This enum defines the possible grammatical genders assigned
 * to nouns within the system. It is used to support language-based
 * learning features, such as gender identification questions.</p>
 *
 * <ul>
 *   <li>MASCULINE – Represents masculine grammatical gender</li>
 *   <li>FEMININE – Represents feminine grammatical gender</li>
 * </ul>
 *
 * <p>The enum is stored as a string in the database when used in
 * JPA entities, improving readability and maintainability.</p>
 *
 * <p>This classification supports structured learning and enables
 * targeted assessment of grammatical concepts.</p>
 */
public enum Gender {

    /** Masculine grammatical gender */
    MASCULINE,

    /** Feminine grammatical gender */
    FEMININE
}