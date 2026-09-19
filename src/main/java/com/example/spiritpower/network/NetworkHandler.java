package com.example.spiritpower.network;

import com.example.spiritpower.SpiritPowerMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SpiritPowerMod.MODID, "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, SpiritPowerPacket.class,
                SpiritPowerPacket::encode, SpiritPowerPacket::decode, SpiritPowerPacket::handle);
    }
}
