package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL_INT;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {


    @Mock
    ArtifactRepository artifactRepostitory;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("imageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("imageUrl");

        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testfindByIdSuccess() {
        //Given. Arrange inputs and targets.
        /*
        "id": "1250808601744904192",
        "name": "Invisibility Cloak",
        "description": "An invisibility cloak is used to make the wearer invisible.",
        "imageUrl": "ImageUrl",
         */

        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setOwner(w);

        given(artifactRepostitory.findById("1250808601744904192")).willReturn(Optional.of(a));

        //When. Act on the target behavior.

        Artifact returnedArtifact = artifactService.findById("1250808601744904192");



        //Then. Assert expected outcomes.
        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepostitory, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindByIdNotFound() {
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() ->{
            Artifact returnedArtifact = artifactService.findById("1250808601744904192");
        });

        assertThat(thrown)
                .isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Could ont find artifact with Id 1250808601744904192 :(");
        verify(artifactRepostitory, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindAllSuccess() {
        given(artifactRepostitory.findAll()).willReturn(this.artifacts);

        List<Artifact> actualArtifacts = artifactService.findAll();

        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepostitory, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        Artifact newArtifact= new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Description...");
        newArtifact.setImageUrl("imageUrl");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepostitory.save(newArtifact)).willReturn(newArtifact);

        Artifact savedArtifact =artifactService.save(newArtifact);

        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepostitory, times(1)).save(newArtifact);
    }

    @Test
    void testUpdateSuccess() {
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact update = new Artifact();
        update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        given(artifactRepostitory.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        given(artifactRepostitory.save(oldArtifact)).willReturn(oldArtifact);

        Artifact updatedArtifact = artifactService.update("1250808601744904192", update);

        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepostitory, times(1)).save(oldArtifact);
        verify(artifactRepostitory, times(1)).findById("1250808601744904192");

    }

    @Test
    void testUpdateNotFoudn() {
        Artifact update = new Artifact();
        update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("An invisibility cloak is used to make the wearer invisible.");
        update.setImageUrl("ImageUrl");

        given(artifactRepostitory.findById("1250808601744904192")).willReturn(Optional.empty());

        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.update("1250808601744904192", update);
        });

        verify(artifactRepostitory, times(1)).findById("1250808601744904192");
    }

    @Test
    void testDeleteSuccess() {
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl");

        given(artifactRepostitory.findById("1250808601744904192")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepostitory).deleteById("1250808601744904192");

        artifactService.delete("1250808601744904192");

        verify(artifactRepostitory, times(1)).deleteById("1250808601744904192");
    }

    @Test
    void testDeleteNotFound() {
        given(artifactRepostitory.findById("1250808601744904192")).willReturn(Optional.empty());

        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.delete("1250808601744904192\"");
        });

        verify(artifactRepostitory, times(1)).deleteById("1250808601744904192");
    }

}