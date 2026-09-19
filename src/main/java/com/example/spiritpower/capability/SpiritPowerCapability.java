package com.example.spiritpower.capability;

import net.minecraft.nbt.CompoundTag;

public class SpiritPowerCapability implements ISpiritPower {
    public static final String NBT_SPIRIT = "SpiritPower";
    public static final String NBT_MAX_SPIRIT = "MaxSpiritPower";

    private int spiritPower = 2;
    private int maxSpiritPower = 2;
    private boolean dirty = true;

    @Override public int getSpiritPower() { return spiritPower; }
    @Override public void setSpiritPower(int value) { this.spiritPower = clamp(value, 0, maxSpiritPower); this.dirty = true; }
    @Override public int getMaxSpiritPower() { return maxSpiritPower; }
    @Override public void setMaxSpiritPower(int max) { this.maxSpiritPower = Math.max(2, Math.min(40, max)); if (spiritPower > maxSpiritPower) spiritPower = maxSpiritPower; this.dirty = true; }
    @Override public int addSpiritPower(int amount) { if (amount <= 0) return 0; int before = spiritPower; spiritPower = clamp(spiritPower + amount, 0, maxSpiritPower); dirty = true; return spiritPower - before; }
    @Override public boolean consumeSpiritPower(int amount) { if (amount <= 0) return true; if (spiritPower < amount) return false; spiritPower -= amount; dirty = true; return true; }
    @Override public void restoreFull() { spiritPower = maxSpiritPower; dirty = true; }
    @Override public void setDirty(boolean d) { dirty = d; }
    @Override public boolean isDirty() { return dirty; }
    @Override public void copyFrom(ISpiritPower other) { this.spiritPower = other.getSpiritPower(); this.maxSpiritPower = other.getMaxSpiritPower(); this.dirty = true; }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_SPIRIT, spiritPower);
        tag.putInt(NBT_MAX_SPIRIT, maxSpiritPower);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains(NBT_SPIRIT)) spiritPower = tag.getInt(NBT_SPIRIT);
        if (tag.contains(NBT_MAX_SPIRIT)) maxSpiritPower = tag.getInt(NBT_MAX_SPIRIT);
        dirty = true;
    }

    private static int clamp(int v, int min, int max) { return Math.max(min, Math.min(max, v)); }
}
