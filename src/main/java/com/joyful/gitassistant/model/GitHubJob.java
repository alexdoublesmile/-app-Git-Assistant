package com.joyful.gitassistant.model;

import com.joyful.gitassistant.view.GitHubView.GitHubUI;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GitHubJob {
    private final GitHub gitHub;
    private final GitHubUI gui = new GitHubUI();
    private final Set<Long> storedPRIds = new HashSet<>();

    public GitHubJob() {

        try {
            gitHub = new GitHubBuilder()
                    .withAppInstallationToken(System.getenv("GITHUB_TOKEN"))
                    .build();

            init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws IOException {
        GHMyself myself = gitHub.getMyself();
        String login = myself.getLogin();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Set<GHPullRequest> newPRs = new HashSet<>();
                    boolean firstStart = storedPRIds.isEmpty();
                    List<RepositoryDescription> repos = myself.getAllRepositories().values()
                            .stream()
                            .map(repo -> {
                                List<GHPullRequest> ghPullRequests = null;

                                try {
                                    ghPullRequests = repo.queryPullRequests()
                                            .list()
                                            .toList();

                                    Set<Long> ghPRIds = ghPullRequests.stream()
                                            .map(GHPullRequest::getId)
                                            .collect(Collectors.toSet());

                                    ghPRIds.removeAll(storedPRIds);
                                    storedPRIds.addAll(ghPRIds);


                                    ghPullRequests.forEach(pr -> {
                                        if (ghPRIds.contains(pr.getId())) {
                                            newPRs.add(pr);
                                        }
                                    });

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                return RepositoryDescription.builder()
                                        .name(repo.getFullName())
                                        .repo(repo)
                                        .pullRequests(ghPullRequests)
                                        .build();
                            }).collect(Collectors.toList());

                    gui.setMenu(login, repos);

//                    if (!firstStart) {
                        newPRs.forEach(pr -> {
                            gui.showNotification("New PR in " + pr.getRepository().getFullName(),
                                    pr.getTitle());
                            System.out.println("New PR in " + pr.getRepository().getFullName());
                        });
//                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000, 1000);
    }
}
