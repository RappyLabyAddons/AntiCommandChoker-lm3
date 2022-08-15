package com.rappytv.anticmdchoker.listener;

import com.google.common.util.concurrent.ListenableFuture;
import com.rappytv.anticmdchoker.AntiCommandChoker;
import net.labymod.api.events.MessageSendEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public final class MessageSendListener implements MessageSendEvent {
    private final List<IMessageSendSubscriber> messageSendSubscribers;

    private MessageSendListener(List<IMessageSendSubscriber> messageSendSubscribers) {
        this.messageSendSubscribers = messageSendSubscribers;
    }

    public static MessageSendListener create(IMessageSendSubscriber... messageSendSubscribers) {
        MessageSendListener messageSendListener = new MessageSendListener(new LinkedList<>());
        IMessageSendSubscriber[] subs = messageSendSubscribers;
        int subLength = messageSendSubscribers.length;

        for(int i = 0; i < subLength; ++i) {
            IMessageSendSubscriber messageSendSubscriber = subs[i];
            messageSendListener.registerSubscriber(messageSendSubscriber);
        }

        return messageSendListener;
    }

    private void registerSubscriber(IMessageSendSubscriber messageSendSubscriber) {
        this.messageSendSubscribers.add(messageSendSubscriber);
    }

    public boolean onSend(final String s) {
        if(!AntiCommandChoker.enabled) return false;
        if(this.messageSendSubscribers != null && !this.messageSendSubscribers.isEmpty()) {
            final String command = s.split(" ")[0].trim();

            for(IMessageSendSubscriber messageSendSubscriber : this.messageSendSubscribers) {
                ListenableFuture<Boolean> subscriberFuture = AntiCommandChoker.getInstance().getAsyncListeningExecutor().submit(new Callable<Boolean>() {
                    public Boolean call() {
                        return messageSendSubscriber.onSend(command, s);
                    }
                });

                try {
                    boolean isCancelled = (Boolean) subscriberFuture.get();
                    if(isCancelled) {
                        return true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            return false;
        } else {
            return false;
        }
    }
}
