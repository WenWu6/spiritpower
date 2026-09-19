package com.example.spiritpower.capability;

import com.example.spiritpower.SpiritPowerMod;
import com.example.spiritpower.network.NetworkHandler;
import com.example.spiritpower.network.SpiritPowerPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SpiritPowerMod.MODID)
public class SpiritPowerEvents {
    private static final ResourceLocation CAP_ID = new ResourceLocation(SpiritPowerMod.MODID, "spirit_power");

    public static int calcMaxFromLevel(int level) {
        int bonus = (level / 30) * 2;
        return Math.min(40, 2 + bonus);
    }

    @SubscribeEvent
    public static void onAttach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            SpiritPowerProvider p = new SpiritPowerProvider();
            event.addCapability(CAP_ID, p);
            event.addListener(p::invalidate);
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(SpiritPowerProvider.SPIRIT_POWER_CAPABILITY)
                .ifPresent(old -> event.getEntity().getCapability(SpiritPowerProvider.SPIRIT_POWER_CAPABILITY)
                        .ifPresent(n -> n.copyFrom(old)));
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            sp.getCapability(SpiritPowerProvider.SPIRIT_POWER_CAPABILITY).ifPresent(cap -> {
                int newMax = calcMaxFromLevel(sp.experienceLevel);
                if (newMax != cap.getMaxSpiritPower()) {
                    cap.setMaxSpiritPower(newMax);
                }
                sync(sp, cap);
            });
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;
        sp.getCapability(SpiritPowerProvider.SPIRIT_POWER_CAPABILITY).ifPresent(cap -> {
            int newMax = calcMaxFromLevel(sp.experienceLevel);
            if (newMax != cap.getMaxSpiritPower()) {
                cap.setMaxSpiritPower(newMax);
            }
            if (cap.isDirty()) {
                cap.setDirty(false);
                sync(sp, cap);
            }
        });
    }

    private static void sync(ServerPlayer sp, ISpiritPower cap) {
        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp),
                new SpiritPowerPacket(cap.getSpiritPower(), cap.getMaxSpiritPower()));
    }
}