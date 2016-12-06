package org.keycloak.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class Db {

    private List<Message> messages = new CopyOnWriteArrayList<>();

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public void add(String message, String username) {
        Message record = new Message();
        record.setText(message);
        record.setUsername(username);
        messages.add(record);
    }


    public static class Message {

        private String text;
        private String username;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
