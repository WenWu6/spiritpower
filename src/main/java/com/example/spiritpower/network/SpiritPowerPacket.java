package com.example.spiritpower.network;

import com.example.spiritpower.capability.SpiritPowerProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpiritPowerPacket {
    private final int spiritPower;
    private final int maxSpiritPower;

    public SpiritPowerPacket(int spiritPower, int maxSpiritPower) {
        this.spiritPower = spiritPower;
        this.maxSpiritPower = maxSpiritPower;
    }

    public static void encode(SpiritPowerPacket p, FriendlyByteBuf buf) {
        buf.writeVarInt(p.spiritPower);
        buf.writeVarInt(p.maxSpiritPower);
    }

    public static SpiritPowerPacket decode(FriendlyByteBuf buf) {
        return new SpiritPowerPacket(buf.readVarInt(), buf.readVarInt());
    }

    public static void handle(SpiritPowerPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(SpiritPowerProvider.SPIRIT_POWER_CAPABILITY).ifPresent(cap -> {
                    cap.setSpiritPower(p.spiritPower);
                    cap.setMaxSpiritPower(p.maxSpiritPower);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
