package com.rappytv.anticmdchoker.util;

import net.labymod.core.LabyModCore;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public final class ChatMessageUtil {
    public static void sendMessage(String message) {
        sendMessage(message, null, null);
    }

    public static void sendMessage(String message, String hover, String command) {
        LabyModCore.getMinecraft()
                .getPlayer()
                .sendMessage(getIChatComponent(message, hover, command));
    }

    private static ITextComponent getIChatComponent(String message, String hover, String command) {
        final ITextComponent iChatComponent = new TextComponentString(message);
        final Style chatStyle = new Style();
        if (hover != null) {
            chatStyle.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new TextComponentString(hover)));
        }
        if (command != null) {
            chatStyle.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    command));
        }
        iChatComponent.setStyle(chatStyle);
        return iChatComponent;
    }
}