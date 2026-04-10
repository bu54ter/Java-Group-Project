package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

/**
 * Enumeration representing the different types of questions
 * that can be used within the system.
 *
 * <p>This enum is used to categorise questions based on their
 * purpose or learning objective, allowing the application to
 * handle different question formats appropriately.</p>
 *
 * <ul>
 *   <li>GENDER – Questions related to grammatical gender</li>
 *   <li>MEANING – Questions testing understanding of word meanings</li>
 *   <li>TRANSLATE – Questions requiring translation between languages</li>
 * </ul>
 *
 * <p>This classification supports flexible test generation and
 * enables tailored feedback and evaluation strategies.</p>
 */
public enum QuestionType {

    /** Question focused on identifying grammatical gender */
    GENDER,

    /** Question focused on understanding or selecting correct meaning */
    MEANING,

    /** Question requiring translation between languages */
    TRANSLATE
}