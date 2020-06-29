package com.yzl.judgehost.core.enumerations;

import java.util.stream.Stream;

/**
 * @author yuzhanglong
 * @date 2020-6-24 19:10:43
 * @description 编译脚本的枚举类
 */

public enum LanguageScriptEnum {
    // for python
    PYTHON("#!/bin/sh\n" +
            "\n" +
            "echo '#!/bin/sh' > RUN_PATH" +
            "\n" +
            "echo 'python3 CODE_PATH' >> RUN_PATH", "py"),

    // for java
    JAVA("#!/bin/sh\n" +
            "\n" +
            "javac CODE_PATH\n" +
            "echo '#!/bin/sh' > RUN_PATH\n" +
            "echo 'java -cp CODE_PATH' >> RUN_PATH", "java"),

    // for c
    C("#!/bin/sh\n" +
            "\n" +
            "gcc -Wall -O2 -std=gnu11 CODE_PATH -o RUN_PATH -lm", "c"),


    // for cpp
    C_PLUS_PLUS("#!/bin/sh\n" +
            "\n" +
            "g++ -Wall -O2 -std=gnu++17 CODE_PATH -o RUN_PATH", "cpp");

    public String getBuildScript() {
        return buildScript;
    }

    public String getBuildScriptByRunningPath(String codePath, String runningPath) {
        String script = getBuildScript();
        script = script.replace("RUN_PATH", runningPath).replace("CODE_PATH", codePath);
        return script;
    }


    public String getExtensionName() {
        return extensionName;
    }

    private final String buildScript;
    private final String extensionName;

    LanguageScriptEnum(String buildScript, String extensionName) {
        this.buildScript = buildScript;
        this.extensionName = extensionName;
    }

    public static LanguageScriptEnum toLanguageType(String language) {
        return Stream.of(LanguageScriptEnum.values())
                .filter(c -> c.toString().equals(language))
                .findAny()
                .orElse(null);
    }

    /**
     * @param languageName 语言的名称，例如：JAVA
     * @return Boolean 是否支持传入的语言
     * @author yuzhanglong
     * @date 2020-6-28 14:39:00
     * @description 判断是否支持某个该语言
     */
    public static Boolean isLanguageAccepted(String languageName) {
        return toLanguageType(languageName) != null;
    }
}