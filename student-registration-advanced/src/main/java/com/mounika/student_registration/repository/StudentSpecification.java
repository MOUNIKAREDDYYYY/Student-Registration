package com.mounika.student_registration.repository;

import com.mounika.student_registration.models.Student;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification class leveraging the JPA Criteria API.
 * Provides dynamic, type-safe filtering for students without requiring
 * separate query methods for every combination of search parameters.
 */
public final class StudentSpecification {

    private StudentSpecification() {
        // Prevent instantiation
    }

    /**
     * Builds a composite Specification based on the provided optional criteria.
     * Any null or empty filter parameter is ignored.
     *
     * @param name Optional student name keyword (case-insensitive substring)
     * @param course Optional course name (case-insensitive match)
     * @param minAge Optional minimum age inclusive
     * @param maxAge Optional maximum age inclusive
     * @return JPA Specification predicate
     */
    public static Specification<Student> filterBy(String name, String course, Integer minAge, Integer maxAge) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("studentName")),
                        "%" + name.trim().toLowerCase() + "%"
                ));
            }

            if (course != null && !course.trim().isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("courseName")),
                        "%" + course.trim().toLowerCase() + "%"
                ));
            }

            if (minAge != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("age"), minAge));
            }

            if (maxAge != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("age"), maxAge));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
