package net.shulker.tool;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

import static net.shulker.tool.SharedVariables.shouldDupe;
import static net.shulker.tool.SharedVariables.shouldDupeAll;
import static net.shulker.tool.Util.CLIENT;
import static net.shulker.tool.Util.log;

public class MainClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, player, context) -> {
            if (screen instanceof ShulkerBoxScreen) {
                ScreenKeyboardEvents.afterKeyPress(screen).register((screen1, key, scancode, modifier) -> {
                    if (!shouldDupe || !shouldDupeAll) {
                        if (Screen.hasControlDown() && key == GLFW.GLFW_KEY_D) {
                            if (Screen.hasShiftDown()) {
                                if (shouldDupe) shouldDupe = false;
                                shouldDupeAll = true;
                            } else {
                                if (shouldDupeAll) shouldDupeAll = false;
                                shouldDupe = true;
                            }
                        }
                    }
                });
            }
        });
    }

    public static boolean fra = true;

    public static void setFra(boolean fra) {
        MainClient.fra = fra;
    }

    public static void tick() {

        boolean b1 = (CLIENT.player.currentScreenHandler instanceof ShulkerBoxScreenHandler);

        if (shouldDupe || shouldDupeAll) {
            HitResult hit = CLIENT.crosshairTarget;
            if (hit instanceof BlockHitResult) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                if (CLIENT.world.getBlockState(blockHit.getBlockPos()).getBlock() instanceof ShulkerBoxBlock && b1) {
                    CLIENT.interactionManager.updateBlockBreakingProgress(blockHit.getBlockPos(), Direction.DOWN);
                } else {
                    log("You need to have a shulker box screen open and look at a shulker box.");
                    CLIENT.player.closeHandledScreen();
                    shouldDupe = false;
                    shouldDupeAll = false;
                }
            }
        }
        if (b1) {

        } else {
            setFra(true);
        }
    }
}
