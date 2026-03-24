package edu.tcu.cs.hogwartsartifactsonline.wizard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class WizardRepository extends JpaRepository <Wizard, Integer> {
}
