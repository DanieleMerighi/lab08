package it.unibo.deathnote.impl;

import it.unibo.deathnote.api.DeathNote;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DeathNoteImpl implements DeathNote {

    private Map<String, Death> deathNote = new LinkedHashMap<>();
    private String currentName;

    @Override
    public String getRule(int ruleNumber) {
        if (ruleNumber <= 0 || ruleNumber > DeathNote.RULES.size())
            throw new IllegalArgumentException(
                    "IllegalArgumentException for ruleNumber smaller than 1 or larger than the number of rules");
        return DeathNote.RULES.get(ruleNumber - 1);
    }

    @Override
    public void writeName(String name) {
        // throws exception if null
        Objects.requireNonNull(name);
        currentName = name;
        deathNote.put(name, new Death());
    }

    @Override
    public boolean writeDeathCause(String cause) {
        if (cause == null || currentName == null) {
            throw new IllegalStateException("Name and details must be specified");
        }
        final var currDeath = deathNote.get(currentName);
        final var newDeath = currDeath.writeCause(cause);
        if (!currDeath.equals(newDeath)) {
            deathNote.put(currentName, newDeath);
            return true;
        }
        return false;
    }

    @Override
    public boolean writeDetails(String details) {
        if (details == null || currentName == null) {
            throw new IllegalStateException("Name and details must be specified");
        }
        final var currDeath = deathNote.get(currentName);
        final var newDeath = currDeath.writeDetails(details);
        if (!currDeath.equals(newDeath)) {
            deathNote.put(currentName, newDeath);
            return true;
        }
        return false;
    }

    @Override
    public String getDeathCause(String name) {
        return getDeath(name).cause;
    }

    @Override
    public String getDeathDetails(String name) {
        return getDeath(name).details;
    }

    @Override
    public boolean isNameWritten(String name) {
        return deathNote.containsKey(name);
    }

    private Death getDeath(final String name) {
        final var death = deathNote.get(name);
        if (death == null) {
            throw new IllegalArgumentException(name + "has never been written in this book");
        }
        return death;
    }

    private static class Death {
        private static final String DEFAULT_CAUSE = "heart attack";
        private static final short ACCEPTED_TIME_CAUSE = 40;
        private static final short ACCEPTED_TIME_DETAILS = 6000 + ACCEPTED_TIME_CAUSE;
        private String cause;
        private String details;
        private final long timeOfDeath;

        private Death(final String cause, final String details) {
            this.cause = cause;
            this.details = details;
            timeOfDeath = System.currentTimeMillis();
        }

        Death() {
            this(DEFAULT_CAUSE, "");
        }

        private Death writeCause(final String cause) {
            return System.currentTimeMillis() < timeOfDeath + ACCEPTED_TIME_CAUSE
                    ? new Death(cause, this.details)
                    : this;
        }

        private Death writeDetails(final String details) {
            return System.currentTimeMillis() < timeOfDeath + ACCEPTED_TIME_DETAILS
                    ? new Death(this.cause, details)
                    : this;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((cause == null) ? 0 : cause.hashCode());
            result = prime * result + ((details == null) ? 0 : details.hashCode());
            result = prime * result + (int) (timeOfDeath ^ (timeOfDeath >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Death death = (Death) o;
            return timeOfDeath == death.timeOfDeath &&
                    Objects.equals(cause, death.cause) &&
                    Objects.equals(details, death.details);
        }

    }

}
