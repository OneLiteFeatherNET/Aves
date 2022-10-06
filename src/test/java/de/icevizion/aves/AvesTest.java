package de.icevizion.aves;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AvesTest {

    @Test
    void testAves() {
       var avesInstance = new Aves();
       Mockito.doNothing().when(avesInstance).initialize();
       Mockito.doNothing().when(avesInstance).terminate();
       assertNotNull(avesInstance);
    }
}