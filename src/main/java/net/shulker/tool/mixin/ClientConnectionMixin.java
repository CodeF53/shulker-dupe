package net.shulker.tool.mixin;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.shulker.tool.SharedVariables.*;
import static net.shulker.tool.Util.*;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(at = @At("TAIL"), method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", cancellable = true)
    public void send(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> callback, CallbackInfo ci) {
        if (packet instanceof PlayerActionC2SPacket) {
            if (((PlayerActionC2SPacket) packet).getAction() == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
                if (shouldDupe) {
                    quickMoveItem(0);
                    shouldDupe = false;
                } else if (shouldDupeAll) {
                    quickMoveAllItems();
                    shouldDupeAll = false;
                }
            }
        }
    }
}
