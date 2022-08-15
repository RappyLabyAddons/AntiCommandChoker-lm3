package com.rappytv.anticmdchoker.listener.subscriber;

import com.rappytv.anticmdchoker.AntiCommandChoker;
import com.rappytv.anticmdchoker.listener.IMessageSendSubscriber;
import com.rappytv.anticmdchoker.util.ChatMessageUtil;
import net.labymod.core.LabyModCore;

import java.util.Random;

public final class SendAnywaySubscriber implements IMessageSendSubscriber {
    private static final String commandTriggerPrefix = "$acc$send-anyway-";
    private static String commandTriggerSuffix;
    private String lastSent;

    public static String getCommandTriggerSuffix() {
        return commandTriggerPrefix + commandTriggerSuffix;
    }

    public static void redefineCommandTriggerSuffix() {
        final int min = 100;
        final int max = 999;
        final String newSuffix = String.valueOf(new Random().nextInt(max - min + 1) + min);
        if (commandTriggerSuffix != null && commandTriggerSuffix.equalsIgnoreCase(newSuffix)) {
            redefineCommandTriggerSuffix();
            return;
        }
        commandTriggerSuffix = newSuffix;
    }

    @Override
    public boolean onSend(String command, String wholeMessage) {
        if (!command.startsWith(commandTriggerPrefix)) {
            return false;
        }
        if (command.equalsIgnoreCase(getCommandTriggerSuffix())) {
            final String toSend = wholeMessage.replace(getCommandTriggerSuffix(), "").trim();
            if (toSend.equals(lastSent)) {
                ChatMessageUtil.sendMessage(AntiCommandChoker.prefix + "\u00A7cThe message has already been sent!");
                return true;
            }
            lastSent = toSend;
            LabyModCore.getMinecraft().getPlayer().sendChatMessage(toSend);
            return true;
        }
        ChatMessageUtil.sendMessage(AntiCommandChoker.prefix + "\u00A7cSend action for this choked command is already expired or could not be found!");
        return true;
    }
}