package com.yzl.judgehost.core.enumerations;

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
            "javac hello.java\n" +
            "echo '#!/bin/sh' > run\n" +
            "echo 'java code' >> run", "java"),

    // for c
    C("gcc -Wall -O2 -std=gnu11 'code.c' -o run -lm", "c"),


    // for cpp
    C_PLUS_PLUS("g++ -Wall -O2 -std=gnu++17 'code.cpp' -o run", "cpp");

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
}
