package com.joyful.gitassistant.view;

import com.joyful.gitassistant.model.RepositoryDescription;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

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

    public void openBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void showNotification(String title, String text) {
        gitHubIcon.displayMessage(title, text, TrayIcon.MessageType.INFO);
    }

    public void setMenu(String login, List<RepositoryDescription> repos) {
        PopupMenu popup = new PopupMenu();

        MenuItem accountMenuItem = new MenuItem(login);
        accountMenuItem.addActionListener(e -> openBrowser("https://github.com/" + login));

        MenuItem notificationMenuItem = new MenuItem("notifications");
        notificationMenuItem.addActionListener(e -> openBrowser("https://github.com/notifications"));

        Menu repositoriesMenu = new Menu("repositories");
        repos.forEach(repo -> {
            String name = repo.getPullRequests().isEmpty()
                    ? repo.getName()
                    : String.format("(%d) %s", repo.getPullRequests().size(), repo.getName());
            Menu repoMenu = new Menu(name);

            MenuItem openBrowserItem = new MenuItem("Open in browser");
            openBrowserItem.addActionListener(e -> openBrowser(repo.getRepo().getHtmlUrl().toString()));
            repoMenu.add(openBrowserItem);

            if (!repo.getPullRequests().isEmpty()) {
                repoMenu.addSeparator();
            }

            repo.getPullRequests().forEach(pr -> {
                MenuItem prMenuItem = new MenuItem(pr.getTitle());
                prMenuItem.addActionListener(e -> openBrowser(pr.getHtmlUrl().toString()));

                repoMenu.add(prMenuItem);
            });

            repositoriesMenu.add(repoMenu);

        });

        popup.add(accountMenuItem);
        popup.addSeparator();
        popup.add(notificationMenuItem);
        popup.addSeparator();
        popup.add(repositoriesMenu);

        gitHubIcon.setPopupMenu(popup);
    }
}
