import java.util.*

plugins {
    `base-plugin-kotlin`
    application
}

group = "io.github.thanosfisherman.blazeworks.desktop"
version = "1.0-SNAPSHOT"

dependencies {
    addDesktopDependencies()
    addGraalDesktopDependencies()
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    val os = System.getProperty("os.name").lowercase(Locale.getDefault())
    if (os.contains("mac"))
        applicationDefaultJvmArgs += listOf("-XstartOnFirstThread")
    val classname = "io.github.thanosfisherman.blazeworks.desktop.DesktopLauncher"
    mainClass.set(classname)
}

registerDesktopTasks(application.mainClass.get())
