package com.rappytv.anticmdchoker.listener.subscriber;

import com.rappytv.anticmdchoker.AntiCommandChoker;
import com.rappytv.anticmdchoker.listener.IMessageSendSubscriber;
import com.rappytv.anticmdchoker.util.ChatMessageUtil;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;

import java.util.regex.Pattern;

public final class CommandChokerSubscriber implements IMessageSendSubscriber {
    public CommandChokerSubscriber() {
    }

    public boolean onSend(String command, String wholeMessage) {
        if(!AntiCommandChoker.enabled) return false;
        String chokeTrigger = "7";
        if(Pattern.matches("[a-zA-Z]+", command.replace(chokeTrigger, "")) && command.length() > 1 && command.startsWith(chokeTrigger)) {
            if(AntiCommandChoker.replaceChokeTrigger) {
                SendAnywaySubscriber.redefineCommandTriggerSuffix();
                ChatMessageUtil.sendMessage(AntiCommandChoker.prefix + "\u00A7cRecognized choked command!");
                ChatMessageUtil.sendMessage(AntiCommandChoker.prefix + "\u00A7b[SEND MESSAGE ANYWAY]", "\u00A7a" + wholeMessage, String.format(SendAnywaySubscriber.getCommandTriggerSuffix() + " %s", wholeMessage));
                return true;
            } else {
                Minecraft.getMinecraft().player.sendChatMessage("/" + wholeMessage.substring(1));
                ChatMessageUtil.sendMessage(AntiCommandChoker.prefix + "\u00A7aSuccessfully prevented you from choking your command!");
                return true;
            }
        } else {
            return false;
        }
    }
}
