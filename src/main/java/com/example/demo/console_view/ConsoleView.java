package com.example.demo.console_view;

import com.example.demo.model.*;

import java.util.Scanner;
import java.util.UUID;

public class ConsoleView {
    private Scanner sc;
    private Account account;

    public void run() {
        sc = Scan.getInstance().getScanner();
        showStartMessage();
        while (true) {
            String input = sc.nextLine();
            parseCommand(input.toUpperCase());
        }
    }

    private void parseCommand(String command) {
        if (command.equals("REGISTER")) {
            register();
        } else if (command.equals("LOGIN")) {
            login();
        } else if (command.equals("FORGET PASSWORD")) {

        } else if (command.equals("EXIT")) {
            DB.saveData();
            System.exit(0);
        } else if (command.equals("HELP")) {
            System.out.println("Login page commands:\n. LOGIN\n. REGISTER\n. FORGET PASSWORD\n");
        }
        if (account == null) {
            System.out.println("You must login first!");
            return;
        }
        if (command.equals("CREATE POST")) {
            createPost();
        } else if (command.equals("LIKE POST")) {
            likeOrDislikePost(true);
        } else if (command.equals("DISLIKE POST")) {
            likeOrDislikePost(false);
        } else if (command.equals("SHOW POSTS")) {
            showPosts();
        } else if (command.equals("SHOW A SINGLE POST")) {
            showSinglePost();
        } else if (command.equals("COMMENT")) {
            makeComment();
        } else if (command.equals("SHOW COMMENT")) {
            showComments();
        } else if (command.equals("WATCH PROFILE")) {
            watchProfile();
        } else if (command.equals("SHOW FOLLOWERS")) {
            showFollowers();
        } else if (command.equals("SHOW FOLLOWINGS")) {
            showFollowings();
        } else if (command.equals("LIKE COMMENT")) {
            likeOrDislikeComment(true);
        } else if (command.equals("DISLIKE COMMENT")) {
            likeOrDislikeComment(false);
        } else if (command.equals("REPLY COMMENT")) {
            replyComment();
        }else if (command.equals("SHOW PRIVATE CHAT")) { // ok
            showPrivateChat();
        } else if (command.equals("SHOW STATE")) {
            showState();
        } else if (command.equals("SEND MESSAGE")) { // ok
            sendMessage();
        } else if (command.equals("CREATE GROUP")) { // ok
            createGroup();
        } else if (command.equals("GET GROUPS ID")) { // ...
            getGroupsId();
        } else if (command.equals("ADD USER")) { // ok
            addUser();
        } else if (command.equals("SHOW GROUP CHAT")) { // ok
            showGroupChat();
        } else if (command.equals("SHOW MEMBERS")) { // ok
            showMembers();
        } else if (command.equals("FORWARD MESSAGE")) {
            forwardMessage();
        } else if (command.equals("EDIT MESSAGE")) { // ok
            editMessage();
        } else if (command.equals("DELETE MESSAGE")) { // ok
            deleteMessage();
        } else if (command.equals("REMOVE USER")) { // ok
            removeUser();
        } else if (command.equals("BAN USER")) { // ok
            banUser();
        } else if (command.equals("BLOCK USER")) { // ok
            blockUser();
        } else if (command.equals("CHANGE GROUP NAME")) { // ok
            renameGroup();
        } else if (command.equals("CHANGE GROUP ID")) { // ...
            changeGroupId();
        }
    }

    private void changeGroupId() {
        System.out.println("Enter the name of the group:");
        String groupName = sc.nextLine();
        if (GroupChat.isExist(groupName)) {
            if (GroupChat.isOwner(this.account, groupName)) {
                GroupChat groupChat = GroupChat.getGroupChatByName(groupName);
                groupChat.setUuid(UUID.randomUUID());
                System.out.println("Changed successfully!");
            } else {
                System.out.println("You can't change the id of this group!");
            }
        } else {
            System.out.println("No group with this name!");
        }
    }

    private void getGroupsId() {
        boolean hasGroup = false;
        for (GroupChat userGroup : GroupChat.getUserGroups(this.account)) {
            System.out.println(userGroup.getName() + " : " + userGroup.getUuid());
            hasGroup = true;
        }
        if (!hasGroup) {
            System.out.println("You haven't join in any group!");
        }
    }

    private void renameGroup() {
        System.out.println("Enter the name of the group:");
        String groupName = sc.nextLine();
        if (GroupChat.isExist(groupName)) {
            if (GroupChat.isOwner(this.account, groupName)) {
                System.out.println("Enter the new name:");
                String name = sc.nextLine();
                if (!GroupChat.isExist(name)) {
                    GroupChat.getGroupChatByName(groupName).setName(name);
                    System.out.println("Renamed successfully!");
                } else {
                    System.out.println("This name is not available!");
                }
            } else {
                System.out.println("You can't rename this group!");
            }
        } else {
            System.out.println("No group with this name!");
        }
    }

    private void blockUser() {
        System.out.println("Enter the username of the account you wanna block:");
        String username = sc.nextLine();
        Account account = Account.getAccount(username);
        if (account == null) {
            System.out.println("Username not found!");
            return;
        }
        this.account.blockUser(account);
        System.out.println("blocked successfully!");
    }

    private void banUser() {
        System.out.println("Enter the name of the group:");
        String groupName = sc.nextLine();
        if (GroupChat.isExist(groupName)) {
            if (GroupChat.isOwner(this.account, groupName)) {
                System.out.println("Enter the username of the account you wanna ban:");
                String username = sc.nextLine();
                Account account = Account.getAccount(username);
                if (account == null) {
                    System.out.println("Username not found!");
                    return;
                }
                GroupChat groupChat = GroupChat.getGroupChatByName(groupName);
                if (GroupChat.isMember(account, groupName)) {
                    groupChat.banUser(account);
                    System.out.println("banned successfully!");
                } else {
                    System.out.println("There is no member with this username in this group!");
                }
            } else {
                System.out.println("You can't ban members from this group!");
            }
        } else {
            System.out.println("No group with this name!");
        }
    }

    private void removeUser() {
        System.out.println("Enter the name of the group:");
        String groupName = sc.nextLine();
        if (GroupChat.isExist(groupName)) {
            if (GroupChat.isOwner(this.account, groupName)) {
                System.out.println("Enter the username of the account you wanna remove from this group:");
                String username = sc.nextLine();
                Account account = Account.getAccount(username);
                if (account == null) {
                    System.out.println("Username not found!");
                    return;
                }
                GroupChat groupChat = GroupChat.getGroupChatByName(groupName);
                if (GroupChat.isMember(account, groupName)) {
                    groupChat.removeUser(account);
                    System.out.println("removed successfully!");
                } else {
                    System.out.println("There is no member with this username in this group!");
                }
            } else {
                System.out.println("You can't remove members from this group!");
            }
        } else {
            System.out.println("No group with this name!");
        }
    }

    private void deleteMessage() {
        System.out.println("Enter the uuid of the message you wanna delete:");
        String uuid = sc.nextLine();
        Message message = Message.getMessageById(uuid);
        if (message == null) {
            System.out.println("Not Found!");
            return;
        }
        if (message.getAccount() != this.account) {
            System.out.println("You can't delete this message!");
        } else {
            message.deleteMessage();
            System.out.println("Deleted successfully!");
        }
    }

    private void editMessage() {
        System.out.println("Enter the uuid of the message you wanna edit:");
        String uuid = sc.nextLine();
        Message message = Message.getMessageById(uuid);
        if (message == null) {
            System.out.println("Not Found!");
            return;
        }
        if (message.getAccount() != this.account || message.getForwardedUsername() != null) {
            System.out.println("You can't edit this message!");
        } else {
            System.out.println("Enter the edited message:");
            String editedMessage = sc.nextLine();
            message.setContent(editedMessage);
            message.setEdited(true);
            System.out.println("Edited successfully!");
        }
    }

    private void forwardMessage() {
        System.out.println("Enter the uuid of the message you wanna forward:");
        String uuid = sc.nextLine();
        Message message = Message.getMessageById(uuid);
        if (message == null) {
            System.out.println("Not Found!");
            return;
        }
        for (Account account2 : Account.getAccounts().values()) {
            if (account2 != this.account) {
                PrivateChat privateChat = PrivateChat.getPrivateChat(this.account, account2);
                for (Message privateChatMessage : privateChat.getMessages()) {
                    if (privateChatMessage.getUuid().toString().equals(uuid)) {
                        forwardMessage(privateChatMessage);
                        return;
                    }
                }
            }
        }
        for (GroupChat userGroup : GroupChat.getUserGroups(this.account)) {
            for (Message userGroupMessage : userGroup.getMessages()) {
                if (userGroupMessage.getUuid().toString().equals(uuid)) {
                    forwardMessage(userGroupMessage);
                    return;
                }
            }
        }

        System.out.println("you can't forward this message!");

    }

    private void forwardMessage(Message message) {
        System.out.println("where do you wanna forward this message? 1- private chat  2- group chat");
        String string = sc.nextLine();
        if (string.equals("1")) {
            System.out.println("Enter the username of the account you wanna forward this message to:");
            String username = sc.nextLine();
            Account account2 = Account.getAccount(username);
            if (account2 == null) {
                System.out.println("Username not found!");
            } else if (account2.getBlockedUsers().contains(this.account)) {
                System.out.println("You are blocked!");
            } else {
                PrivateChat privateChat = PrivateChat.getPrivateChat(this.account, account2);
                if (message.getForwardedUsername() == null) {
                    privateChat.forwardMessage(this.account, message.getContent(), message.getAccount());
                } else {
                    Account forwardedAccount = Account.getAccount(message.getForwardedUsername());
                    privateChat.forwardMessage(this.account, message.getContent(), forwardedAccount);
                }
                System.out.println("forwarded successfully!");
            }
        } else if (string.equals("2")) {
            System.out.println("Enter the name of the group you wanna forward this message to:");
            String groupName = sc.nextLine();
            if (!GroupChat.isExist(groupName)) {
                System.out.println("Not Found!");
            } else {
                if (!GroupChat.isMember(this.account, groupName)) {
                    System.out.println("You aren't join in this group!");
                } else if (GroupChat.getGroupChatByName(groupName).getBannedUsers().contains(this.account)) {
                    System.out.println("You are banned!");
                } else {
                    GroupChat groupChat = GroupChat.getGroupChatByName(groupName);
                    if (message.getForwardedUsername() == null) {
                        groupChat.forwardMessage(this.account, message.getContent(), message.getAccount());
                    } else {
                        Account forwardedAccount = Account.getAccount(message.getForwardedUsername());
                        groupChat.forwardMessage(this.account, message.getContent(), forwardedAccount);
                    }
                    System.out.println("forwarded successfully!");
                }
            }
        }
    }

    private void showMembers() {
        System.out.println("Enter the name of the group:");
        String groupName = sc.nextLine();
        if (!GroupChat.isExist(groupName)) {
            System.out.println("Not founded!");
        } else {
            if (!GroupChat.isMember(this.account, groupName)) {
                System.out.println("You aren't join in this group!");
            } else {
                GroupChat groupChat = GroupChat.getGroupChatByName(groupName);
                for (Account groupChatAccount : groupChat.getAccounts()) {
                    System.out.println(groupChatAccount.getUsername());
                }
            }
        }
    }

    private void showGroupChat() {
        System.out.println("Enter the name of the group:");
        String groupName = sc.nextLine();
        if (!GroupChat.isExist(groupName)) {
            System.out.println("Not founded!");
        } else {
            if (!GroupChat.isMember(this.account, groupName)) {
                System.out.println("You aren't join in this group!");
            } else {
                GroupChat groupChat = GroupChat.getGroupChatByName(groupName);
                boolean haveMessage = false;

                for (Message message : groupChat.getMessages()) {
                    haveMessage = true;
                    if (message.getRepliedUUID() == null && message.getForwardedUsername() == null) {
                        if (message.isEdited()) {
                            System.out.println(message.getAccount().getUsername() + " : " + message.getContent() + " (edited)" + " (" + message.getUuid() + ")");
                        } else {
                            System.out.println(message.getAccount().getUsername() + " : " + message.getContent() + " (" + message.getUuid() + ")");
                        }
                    } else if (message.getRepliedUUID() == null && message.getForwardedUsername() != null) {
                        System.out.println(message.getAccount().getUsername() + " forwarded from " + message.getForwardedUsername() + " : " + message.getContent() + " (" + message.getUuid() + ")");
                    } else if (message.getRepliedUUID() != null && message.getForwardedUsername() == null) {
                        Message repliedMessage = Message.getMessageById(message.getRepliedUUID().toString());
                        if (message.isEdited()) {
                            System.out.println(message.getAccount().getUsername() + " replied to (" + repliedMessage.getContent() + ") : " + message.getContent() + " (edited)" + " (" + message.getUuid() + ")");
                        } else {
                            System.out.println(message.getAccount().getUsername() + " replied to (" + repliedMessage.getContent() + ") : " + message.getContent() + " (" + message.getUuid() + ")");
                        }
                    }
                }
                if (!haveMessage) {
                    System.out.println("No messages yet!");
                }
            }
        }
    }

    private void addUser() {
        System.out.println("Enter the name of the group:");
        String groupName = sc.nextLine();
        if (GroupChat.isExist(groupName)) {
            if (GroupChat.isOwner(this.account, groupName)) {
                System.out.println("Enter the username of the account you wanna add to this group:");
                String username = sc.nextLine();
                Account account = Account.getAccount(username);
                if (account == null) {
                    System.out.println("Username not found!");
                    return;
                }
                for (Account account1 : GroupChat.getGroupChatByName(groupName).getAccounts()) {
                    if (account1.getUsername().equals(account.getUsername())) {
                        System.out.println("this account was joined the group before!");
                        return;
                    }
                }
                GroupChat.getGroupChatByName(groupName).addUser(account);
                System.out.println("added successfully!");
            } else {
                System.out.println("You can't add member to this group!");
            }
        } else {
            System.out.println("No group with this name!");
        }
    }

    private void createGroup() {
        System.out.println("Enter the name of the group:");
        String name = sc.nextLine();
        if (!GroupChat.isExist(name)) {
            account.createGroup(this.account, name);
            System.out.println("created successfully!");
        } else {
            System.out.println("This name is not available!");
        }

    }

    private void sendMessage() {
        System.out.println("1- private chat  2- group chat");
        String inp = sc.nextLine();
        if (inp.equals("1")) {
            sendPrivateMessage();
        } else if (inp.equals("2")) {
            sendGroupMessage();
        }
    }

    private void sendGroupMessage() {
        System.out.println("Enter the name of the group you wanna send message in:");
        String groupName = sc.nextLine();
        if (!GroupChat.isExist(groupName)) {
            System.out.println("Not Found!");
        } else {
            if (!GroupChat.isMember(this.account, groupName)) {
                System.out.println("You aren't join in this group!");
            } else if (GroupChat.getGroupChatByName(groupName).getBannedUsers().contains(this.account)) {
                System.out.println("You are banned!");
            } else {
                System.out.println("if you wanna reply send 'reply' otherwise write your message:");
                String content = sc.nextLine();

                if (content.equals("reply")) {
                    reply(GroupChat.getGroupChatByName(groupName));
                } else {
                    GroupChat groupChat = GroupChat.getGroupChatByName(groupName);
                    groupChat.sendMessage(this.account, content);
                    System.out.println("sent successfully!");
                }
            }
        }
    }

    private void reply(GroupChat groupChat) {
        System.out.println("Enter the uuid of the message you wanna reply:");
        String uuid = sc.nextLine();
        boolean uuidIsExist = false;
        for (Message message : groupChat.getMessages()) {
            if (message.getUuid().toString().equals(uuid)) {
                uuidIsExist = true;
            }
        }
        if (!uuidIsExist) {
            System.out.println("there is no message with this uuid in this chat!");
        } else {
            System.out.println("Enter the message:");
            String content = sc.nextLine();
            groupChat.replyMessage(this.account, content, uuid);
            System.out.println("replied successfully!");
        }
    }

    private void sendPrivateMessage() {
        System.out.println("Enter the username of the account you wanna send message to:");
        String username = sc.nextLine();
        Account account2 = Account.getAccount(username);
        if (account2 == null) {
            System.out.println("Username not found!");
        } else if (account2.getBlockedUsers().contains(this.account)) {
            System.out.println("You are blocked!");
        } else {
            System.out.println("if you wanna reply send 'reply' otherwise write your message:");
            String content = sc.nextLine();

            if (content.equals("reply")) {
                reply(account2);
            } else {
                PrivateChat privateChat = PrivateChat.getPrivateChat(this.account, account2);
                privateChat.sendMessage(this.account, content);
                System.out.println("sent successfully!");
            }
        }
    }

    private void reply(Account account) {
        PrivateChat privateChat = PrivateChat.getPrivateChat(this.account, account);
        System.out.println("Enter the uuid of the message you wanna reply:");
        String uuid = sc.nextLine();
        boolean uuidIsExist = false;
        for (Message message : privateChat.getMessages()) {
            if (message.getUuid().toString().equals(uuid)) {
                uuidIsExist = true;
            }
        }
        if (!uuidIsExist) {
            System.out.println("there is no message with this uuid in this chat!");
        } else {
            System.out.println("Enter the message:");
            String content = sc.nextLine();
            privateChat.replyMessage(this.account, content, uuid);
            System.out.println("replied successfully!");
        }
    }

    private void showPrivateChat() {
        System.out.println("Enter the username of the account you wanna see your chat with:");
        String username = sc.nextLine();
        Account account2 = Account.getAccount(username);
        if (account2 == null) {
            System.out.println("Username not found!");
            return;
        } else {
            showPrivateChat(this.account, account2);
        }
    }

    private void showPrivateChat(Account account1, Account account2) {
        PrivateChat privateChat = PrivateChat.getPrivateChat(account1, account2);
        boolean haveMessage = false;

        for (Message message : privateChat.getMessages()) {
            haveMessage = true;
            if (message.getRepliedUUID() == null && message.getForwardedUsername() == null) {
                if (message.isEdited()) {
                    System.out.println(message.getAccount().getUsername() + " : " + message.getContent() + " (edited)" + " (" + message.getUuid() + ")");
                } else {
                    System.out.println(message.getAccount().getUsername() + " : " + message.getContent() + " (" + message.getUuid() + ")");
                }
            } else if (message.getRepliedUUID() == null && message.getForwardedUsername() != null) {
                System.out.println(message.getAccount().getUsername() + " forwarded from " + message.getForwardedUsername() + " : " + message.getContent() + " (" + message.getUuid() + ")");
            } else if (message.getRepliedUUID() != null && message.getForwardedUsername() == null) {
                Message repliedMessage = Message.getMessageById(message.getRepliedUUID().toString());
                if (message.isEdited()) {
                    System.out.println(message.getAccount().getUsername() + " replied to (" + repliedMessage.getContent() + ") : " + message.getContent() + " (edited)" + " (" + message.getUuid() + ")");
                } else {
                    System.out.println(message.getAccount().getUsername() + " replied to (" + repliedMessage.getContent() + ") : " + message.getContent() + " (" + message.getUuid() + ")");
                }
            }
        }
        if (!haveMessage) {
            System.out.println("No messages yet!");
        }
    }

    private void likeOrDislikeComment(boolean like) {
        Comment comment = getComment();
        if (comment == null) {
            return;
        }
        boolean success;
        if (like) {
            success = comment.like(account);
        } else {
            success = comment.dislike(account);
        }
        if (success)
            System.out.println("Success!");
        else
            System.out.println("comment not found!");
    }

    private void showFollowings() {
        System.out.println("Enter username (self for yourself): ");
        String username = sc.nextLine();
        if (username.equals("self")) {
            if (account.getFollowings().size() == 0) {
                System.out.println("you dont have any followings :(");
            } else {
                System.out.println("Followings : ");
                for (Account account : account.getFollowings()) {
                    System.out.println(account.getUsername());
                }
            }
        } else {
            Account account2 = Account.getAccount(username);
            if (account2 == null) {
                System.out.println("Username not found!");
                return;
            } else {
                if (account2.getFollowings().size() == 0) {
                    System.out.println("you dont have any followings :(");
                } else {
                    System.out.println("Followings : ");
                    for (Account account : account2.getFollowings()) {
                        System.out.println(account.getUsername());
                    }
                }
            }
        }


    }

    private void showFollowers() {
        System.out.println("Enter username (self for yourself): ");
        String username = sc.nextLine();
        if (username.equals("self")) {
            if (account.getFollowers().size() == 0) {
                System.out.println("you dont have any followers :(");
            } else {
                System.out.println("Followers : ");
                for (Account account : account.getFollowers()) {
                    System.out.println(account.getUsername());
                }
            }
        } else {
            Account account2 = Account.getAccount(username);
            if (account2 == null) {
                System.out.println("Username not found!");
                return;
            } else {
                System.out.println("Followers : ");
                for (Account account : account2.getFollowers()) {
                    System.out.println(account.getUsername());
                }
            }
        }
    }

    private void watchProfile() {
        System.out.println("Enter username : ");
        String username = sc.nextLine();
        Account account2 = Account.getAccount(username);
        if (account2 == null) {
            System.out.println("Username not found!");
            return;
        }

        System.out.println("username : " + account2.getUsername());
        System.out.println("Number of Followers : " + account2.getNumberOfFollowers());
        System.out.println("Number of Followings : " + account2.getNumberOfFollowings());
        System.out.println("Number of Groups : " + account2.getNumbersOfGroups());
        System.out.println("Number of Posts : " + account2.getPosts().size());
        if (account2.isBusinessAccount()) {
            account2.addview(this.account);
            account2.showviewSize();
        }
        if (account2.getPosts().size() == 0)
            System.out.println("you dont have any posts :(");
        else {
            System.out.println("recently posted : ");
            if (account2.getPosts().size() > 1) {
                for (int i = account2.getPosts().size() - 1; i < account2.getPosts().size(); i++) {
                    System.out.println(account2.getPosts().get(i));
                    System.out.println("_____________________________");
                }
            } else if (account2.getPosts().size() == 1) {
                System.out.println(account2.getPosts().get(0));
                System.out.println("_____________________________");
            }
            if (account2.getPosts().size() == 0) {
                System.out.println("No posts yet!");
            }
            if (account2.getPosts().size() >= 2) {
                System.out.println("do you want to see all of the posts?(type yes or no)");
                String answer = sc.nextLine();
                if (answer.equals("yes")) {
                    showPosts(account2);
                }
            }


        }

        if (!username.equals(account.getUsername())) {
            System.out.println("Do you want to follow this account?(Type yes or no!)");

            String followOrSkip = sc.nextLine();
            if (followOrSkip.equals("yes")) {
                if (account2.getFollowers().contains(account)) {
                    System.out.println("you have already followed this account!");
                } else {
                    account.follow(account2);
                    System.out.println("you have followed " + username + " :)");
                }
            }

        }
    }

    private void showComments() {
        Post post = getPost();
        if (post == null) {
            return;
        }
        if (post.getComments().size() == 0) {
            System.out.println("there is no comment :(");
        } else {
            for (Comment comment : post.getComments()) {
                System.out.println(comment + "\n_____________________________");
            }
        }

    }

    private void replyComment() {
        System.out.print("Enter comment id: ");
        int commentId;
        try {
            commentId = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Comment id is a number!");
            return ;
        }
        Comment comment = Comment.getCommentById(commentId);
        if (comment == null) {
            System.out.println("Cannot find comment id!");
            return;
        }
        else{
            System.out.println("Enter comment");
            String inp = sc.nextLine();
            comment.writeReplyComment(inp, account);
            System.out.println("Replied comment wrote!");
        }

    }

    private void makeComment() {
        Post post = getPost();
        if (post == null) {
            return;
        }
        System.out.println("Enter comment");
        String inp = sc.nextLine();
        post.writeComment(inp, account);
        System.out.println("Comment wrote!");
    }

    private void showPosts() {
        System.out.print("Enter username (self for yourself): ");
        String username = sc.nextLine();
        if (username.equals("self")) {
            showPosts(this.account);
        } else {
            Account account = Account.getAccount(username);
            if (account == null) {
                System.out.println("Username not found!");
                return;
            }

            showPosts(account);

        }
    }

    private void showSinglePost() {
        System.out.print("Enter username (self for yourself): ");
        String username = sc.nextLine();
        if (username.equals("self")) {
            if (this.account.getPosts().size() == 0) {
                System.out.println("No posts yet!");
                return;
            }
            System.out.print("Enter post id: ");
            int postId;
            try {
                postId = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Post id is a number!");
                return;
            }
            Post post = Post.getPostById(postId);
            if (!this.account.getPosts().contains(post)) {
                System.out.println("post id is wrong!");
            }
            if (post == null) {
                System.out.println("Cannot find post id!");
                return;
            }
            showPost(this.account, postId);
            post.addview(this.account);
        } else {
            Account account2 = Account.getAccount(username);
            if (account2 == null) {
                System.out.println("Username not found!");
                return;
            } else {
                System.out.print("Enter post id: ");
                int postId;
                try {
                    postId = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("Post id is a number!");
                    return;
                }
                Post post = Post.getPostById(postId);
                if (post == null) {
                    System.out.println("Cannot find post id!");
                    return;
                }
                showPost(account2, postId);
                post.addview(this.account);
            }
        }

    }

    private void showPost(Account account, Integer postId) {
        boolean havePost = false;
        for (Post post : account.getPosts()) {
            havePost = true;
            if (post.getId() == postId) {
                System.out.println(post);
                System.out.println("_____________________________");
            }
        }
        if (!havePost) {
            System.out.println("No posts yet!");
        }
    }

    private void showState() {
        if (this.account.isBusinessAccount()) {
            System.out.print("Enter username (self for yourself): ");
            String username = sc.nextLine();
            if (username.equals("self")) {
                if (this.account.getPosts().size() == 0) {
                    System.out.println("No posts yet!");
                    return;
                }
                System.out.print("Enter post id: ");
                int postId;
                try {
                    postId = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("Post id is a number!");
                    return;
                }
                Post post = Post.getPostById(postId);
                if (post == null) {
                    System.out.println("Cannot find post id!");
                }
                post.showviewSize();
            } else {
                System.out.print("Enter post id: ");
                int postId;
                try {
                    postId = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("Post id is a number!");
                    return;
                }
                Post post = Post.getPostById(postId);
                if (post == null) {
                    System.out.println("Cannot find post id!");
                }
                post.showviewSize();
            }

        } else {
            System.out.println("Only business account can see state");
        }

    }

    private void showPosts(Account account) {
        boolean havePost = false;
        for (Post post : account.getPosts()) {
            havePost = true;
            System.out.println(post);
            System.out.println("_____________________________");
            post.addview(this.account);
        }
        if (!havePost) {
            System.out.println("No posts yet!");
        }
    }

    private void likeOrDislikePost(boolean like) {
        Post post = getPost();
        if (post == null) {
            return;
        }
        boolean success;
        if (like) {
            success = post.like(account);
            post.addlikestate(account);
        } else {
            success = post.dislike(account);
        }
        if (success)
            System.out.println("Success!");
        else
            System.out.println("Post not found!");
    }

    private Post getPost() {
        System.out.print("Enter post id: ");
        int postId;
        try {
            postId = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Post id is a number!");
            return null;
        }
        Post post = Post.getPostById(postId);
        if (post == null) {
            System.out.println("Cannot find post id!");
        }
        return post;
    }

    private Comment getComment() {
        System.out.print("Enter comment id: ");
        int commentId;
        try {
            commentId = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Comment id is a number!");
            return null;
        }
        Comment comment = Comment.getCommentById(commentId);
        if (comment == null) {
            System.out.println("Cannot find comment id!");
        }
        return comment;
    }

    private void createPost() {
//        if (account.isBusinessAccount()) {
//            System.out.println("Enter content(is ad): ");
//            String content1 = "ad : ";
//            String content = sc.nextLine();
//            String FinalContent = content1 + content;
//            account.createPost(FinalContent);
//        } else {
//            System.out.println("Enter content(is normal): ");
//            String content = sc.nextLine();
//            account.createPost(content);
//        }
//        System.out.println("Post created!");
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        Account account = Account.checkLogin(username, password);
        if (account == null) {
            System.out.println("Wrong username or password or username!" + "\n" + "Try again!");
            return;
        }
        this.account = account;
        printHelloAccount();
    }

    private void printHelloAccount() {
        System.out.println("Hello " + account.getUsername());
        System.out.println("You have " + account.getNumberOfFollowers() + " followers and " + account.getNumberOfFollowings() + " followings " + "!");
        System.out.println("And you have " + account.getPosts().size() + " posts!");
    }

    private void register() {
        String username;
        String password;
        boolean business;
        while (true) {
            System.out.print("Enter username: ");
            username = sc.nextLine();
            if (Account.usernameExists(username))
                System.out.println("Username is not available, Please try again!");
            else
                break;
        }
        while (true) {
            System.out.print("Enter password: ");
            password = sc.nextLine();
            if (!passwordIsStrong(password))
                System.out.println("Password is weak, Please try again!");
            else
                break;
        }
        while (true) {
            System.out.println("1- Normal 2- Business");
            String inp = sc.nextLine();
            if (inp.equals("1") || inp.equals("2")) {
                business = inp.equals("2");
                break;
            } else {
                System.out.println("Enter option 1 or 2. try again...");
            }

        }
        if (business) {
            account = BusinessAccount.createAccount(username, password);
        } else {
            account = Account.createAccount(username, password);
        }
        printHelloAccount();
    }

    private boolean passwordIsStrong(String password) {
        return true || password.length() >= 8 &&
                !password.toLowerCase().equals(password) &&
                !password.toUpperCase().equals(password) &&
                password.matches(".*[0-9]+.*");
    }

    private void showStartMessage() {
        System.out.println("Hello, Welcome to Jinestagram.");
        System.out.println("For help, type HELP");
    }
}