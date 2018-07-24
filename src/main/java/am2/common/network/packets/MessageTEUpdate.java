package am2.common.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Growlith1223 on 3/4/2017.
 */
public class MessageTEUpdate implements IMessage {
    public NBTTagCompound tag;

    public MessageTEUpdate(NBTTagCompound val){
        this.tag = val;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.tag = ByteBufUtils.readTag(byteBuf);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeTag(byteBuf, this.tag);
    }

    public static class MessageHolder implements IMessageHandler<MessageTEUpdate, IMessage>{
        @Override
        public IMessage onMessage(MessageTEUpdate messageTEUpdate, MessageContext messageContext) {
            Minecraft.getMinecraft().addScheduledTask(()-> {
                NBTTagList list = messageTEUpdate.tag.getTagList("data", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < list.tagCount(); i++){
                    NBTTagCompound tag = list.getCompoundTagAt(i);
                    TileEntity te = Minecraft.getMinecraft().thePlayer.getEntityWorld().getTileEntity(new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z")));
                    if( te != null) {
                        te.readFromNBT(tag);
                        te.markDirty();
                    }
                }
            });
            return null;
        }
    }
}
