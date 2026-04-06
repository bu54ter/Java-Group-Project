package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

/**
 * Enumeration representing user roles within the system.
 *
 * <p>This enum defines the different levels of access and permissions
 * that can be assigned to users. It is used in conjunction with
 * Spring Security to enforce role-based access control (RBAC).</p>
 *
 * <ul>
 *   <li>ADMIN – Full system access and administrative privileges</li>
 *   <li>LECTURER – Access to teaching and content management features</li>
 *   <li>STUDENT – Access to learning stats and assessments</li>
 * </ul>
 *
 * <p>Roles are typically mapped to authorities in the format
 * <code>ROLE_*</code> (e.g., ROLE_ADMIN) for compatibility with
 * Spring Security.</p>
 */
public enum Role {

    /** Administrator role with full access to system features */
    ADMIN,

    /** Lecturer role with permissions to manage educational content */
    LECTURER,

    /** Student role with access to learning and assessment features */
    STUDENT
}