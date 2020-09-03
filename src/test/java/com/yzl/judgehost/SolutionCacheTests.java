package com.yzl.judgehost;

import com.yzl.judgehost.store.redis.JudgeCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SolutionCacheTests {

    @Autowired
    private JudgeCache judgeCache;

    @Test
    void testAddRemotePathToLocalPathMap() {
        String remotePath = "remotePath";
        Map<String, String> localPaths = new HashMap<>();
        localPaths.put("stdIn", "this is standard input local path");
        localPaths.put("stdOut", "this is standard output local path");
        judgeCache.addSolutionRemoteUrlToLocalMap(remotePath, localPaths);
        Map<String, String> content = judgeCache.getSolutionLocalPathByRemoteUrl(remotePath);
        assertThat(content.get("stdIn")).isEqualTo("this is standard input local path");
        assertThat(content.get("stdOut")).isEqualTo("this is standard output local path");
    }

    @Test
    void testReadNullRemotePath() {
        String remotePath = "remotePathNotExist";
        Map<String, String> content = judgeCache.getSolutionLocalPathByRemoteUrl(remotePath);
        assertThat(content).isEqualTo(null);
    }

    @Test
    void testDeleteRemotePathToLocalPathMapItem() {
        String remotePath = "remote path will have been removed";
        Map<String, String> localPaths = new HashMap<>();
        localPaths.put("stdIn", "this is standard input local path");
        localPaths.put("stdOut", "this is standard output local path");
        judgeCache.addSolutionRemoteUrlToLocalMap(remotePath, localPaths);
        Map<String, String> content = judgeCache.getSolutionLocalPathByRemoteUrl(remotePath);
        assertThat(content.get("stdIn")).isEqualTo("this is standard input local path");
        assertThat(content.get("stdOut")).isEqualTo("this is standard output local path");
        judgeCache.removeRemotePathToLocalPathMapItem(remotePath);
        Map<String, String> res = judgeCache.getSolutionLocalPathByRemoteUrl(remotePath);
        assertThat(res).isEqualTo(null);
    }
}