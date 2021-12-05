package com.joyful.gitassistant.view.GitHubView;

import java.awt.*;

public class GitHubUI {

    private final TrayIcon gitHubIcon;

    public GitHubUI() {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage(
                getClass().getResource("/icon/git-hub-icon.png"));

        gitHubIcon = new TrayIcon(image, "Git Assistant");
        gitHubIcon.setImageAutoSize(true);

        try {
            tray.add(gitHubIcon);

        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void showNotification(String title, String text) {
        gitHubIcon.displayMessage(title, text, TrayIcon.MessageType.INFO);
    }
}
