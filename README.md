
gradle-eclipse-checkstyle
--------------------

WARNING: This is a work-in-progress. Version 1.0.0 will indicate its ready to use.

Gradle configuration of Eclipse Checkstyle plugin.  Currently it does the following:

 * Links up the checkstyle config file to Eclipse.
 * Enables / disables exclusion of derived (generated) files.
 * Specifies a list of patterns for files to include / exclude for checking.

```
eclipseCheckstyle {
    excludeDerived = true
    syncFormatter = true
    patterns {
        include "src\/.*\.java"
    }
}
```

