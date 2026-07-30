package com.azoth.somniazodiaca.entities;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

class EntityMappingTests {

    @Test
    void segnoZodiacaleShouldMapToElementoPianetaAndMetallo() throws NoSuchFieldException {
        Field elementoField = SegnoZodiacale.class.getDeclaredField("elemento");
        Field pianetaField = SegnoZodiacale.class.getDeclaredField("pianeta");
        Field metalloField = SegnoZodiacale.class.getDeclaredField("metallo");

        assertNotNull(elementoField.getAnnotation(ManyToOne.class));
        assertNotNull(elementoField.getAnnotation(JoinColumn.class));
        assertNotNull(pianetaField.getAnnotation(ManyToOne.class));
        assertNotNull(pianetaField.getAnnotation(JoinColumn.class));
        assertNotNull(metalloField.getAnnotation(ManyToOne.class));
        assertNotNull(metalloField.getAnnotation(JoinColumn.class));
    }
}
