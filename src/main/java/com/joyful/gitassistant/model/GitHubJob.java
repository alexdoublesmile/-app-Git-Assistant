package com.joyful.gitassistant.model;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

public class GitHubJob {
    private final GitHub gitHub;

    public GitHubJob() {

        try {
            gitHub = new GitHubBuilder()
                    .withAppInstallationToken(System.getenv("GITHUB_TOKEN"))
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
