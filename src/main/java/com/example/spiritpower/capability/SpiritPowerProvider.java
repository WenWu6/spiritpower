package com.example.spiritpower.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpiritPowerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<ISpiritPower> SPIRIT_POWER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private final SpiritPowerCapability backend = new SpiritPowerCapability();
    private final LazyOptional<ISpiritPower> optional = LazyOptional.of(() -> backend);

    @Override public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return SPIRIT_POWER_CAPABILITY.orEmpty(cap, optional);
    }
    @Override public CompoundTag serializeNBT() { return backend.serializeNBT(); }
    @Override public void deserializeNBT(CompoundTag nbt) { backend.deserializeNBT(nbt); }
    public void invalidate() { optional.invalidate(); }
}
