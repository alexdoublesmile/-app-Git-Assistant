package com.joyful.gitassistant.model;

import lombok.Builder;
import lombok.Data;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;

import java.util.List;

@Builder
@Data
public class RepositoryDescription {
    private final String name;
    private final GHRepository repo;
    private final List<GHPullRequest> pullRequests;
}
