package am2.common.network;

import am2.ArsMagica2;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {
    public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ArsMagica2.MODID);

    private static int id = 0;

    public static void registerMessages(){
        //INSTANCE.registerMessage(MessageTEUpdate.MessageHolder.class, MessageTEUpdate.class, id++, Side.CLIENT);
    }
}
