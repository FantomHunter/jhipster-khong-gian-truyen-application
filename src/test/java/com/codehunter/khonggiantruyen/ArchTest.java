package com.codehunter.khonggiantruyen;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.codehunter.khonggiantruyen");

        noClasses()
            .that()
            .resideInAnyPackage("com.codehunter.khonggiantruyen.service..")
            .or()
            .resideInAnyPackage("com.codehunter.khonggiantruyen.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.codehunter.khonggiantruyen.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
