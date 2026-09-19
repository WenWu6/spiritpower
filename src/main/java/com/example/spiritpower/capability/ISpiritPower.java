package com.example.spiritpower.capability;

public interface ISpiritPower {
    int getSpiritPower();
    void setSpiritPower(int value);
    int getMaxSpiritPower();
    void setMaxSpiritPower(int max);
    int addSpiritPower(int amount);
    boolean consumeSpiritPower(int amount);
    void restoreFull();
    void setDirty(boolean dirty);
    boolean isDirty();
    void copyFrom(ISpiritPower other);
}
