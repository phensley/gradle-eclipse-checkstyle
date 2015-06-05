
gradle-eclipse-checkstyle
--------------------

WARNING: This is a work-in-progress. Version 1.0.0 will indicate its ready to use.

Gradle configuration of Eclipse Checkstyle plugin.  Currently it does 2 things:

 * Links up the checkstyle config file to Eclipse.
 * Enables / disables exclusion of derived (generated) files.

```
eclipseCheckstyle {
    syncFormatter = true
    exclude = ['derived']
}
```

