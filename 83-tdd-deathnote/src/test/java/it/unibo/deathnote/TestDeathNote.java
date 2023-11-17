package it.unibo.deathnote;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static java.lang.Thread.sleep;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;

class TestDeathNote {
    private DeathNote deathNote;
    private final String TO_KILL = "Mario Rossi";
    private final String ANOTHER_TO_KILL = "Luigi Bianchi";

    @BeforeEach
    public void setUp() {
        deathNote = new DeathNoteImpl();
    }


    @Test
    public void testIndexRules() {
        try {
            deathNote.getRule(0);
            Assertions.fail("Expected IllegalArgumentException for rule number 0");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().trim().isEmpty());
        }

        try {
            deathNote.getRule(-1);
            Assertions.fail("Expected IllegalArgumentException for negative rule number");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().trim().isEmpty());
        }
    }

    @Test
    public void testEmpyOrNull() {
        for(int i=1; i <= DeathNote.RULES.size(); i++) {
            final var rule = deathNote.getRule(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }
    
    @Test
    public void testActualDeath() {
        assertFalse(deathNote.isNameWritten(TO_KILL));
        deathNote.writeName(TO_KILL);
        assertTrue(deathNote.isNameWritten(TO_KILL));
        assertFalse(deathNote.isNameWritten(ANOTHER_TO_KILL));
        assertFalse(deathNote.isNameWritten(" "));
    }

    @Test
    public void testDeathCause() throws InterruptedException {
        try {
            deathNote.writeDeathCause("Incidente");
            Assertions.fail("Expected IllegalStateException for writing death cause without a name in the DeathNote");
        } catch (IllegalStateException e){
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().trim().isEmpty());
        }

        deathNote.writeName(TO_KILL);
        assertEquals(deathNote.getDeathCause(TO_KILL), "heart attack");
        deathNote.writeName(ANOTHER_TO_KILL);
        assertTrue(deathNote.writeDeathCause("karting accident"));
        assertEquals("karting accident", deathNote.getDeathCause(TO_KILL));
        sleep(100);
        assertFalse(deathNote.writeDeathCause("golf accident"));
        assertEquals("karting accident", deathNote.getDeathCause(TO_KILL));
    }

   @Test
   public void testDeathDetails() throws InterruptedException {
        try{
            deathNote.writeDetails("Chased by Turtles");
            Assertions.fail("Expected IllegalStateException for writing details before writing a name");
        } catch(IllegalStateException e) {
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().trim().isEmpty());
        }
        deathNote.writeName(TO_KILL);
        assertEquals(deathNote.getDeathDetails(TO_KILL), " ");
        assertTrue(deathNote.writeDetails("ran for too long"));
        assertEquals(deathNote.getDeathDetails(TO_KILL), "ran for too long");
        deathNote.writeName(ANOTHER_TO_KILL);
        sleep(6100);
        assertFalse(deathNote.writeDetails("Chased by Turtles"));
        assertTrue(deathNote.writeDetails("ran for too long"));
   }

}