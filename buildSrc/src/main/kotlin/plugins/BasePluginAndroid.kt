package plugins

import BuildType
import Dependencies
import Versions
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import kotlin.collections.set

class BasePluginAndroid : Plugin<Project> {
    override fun apply(project: Project) {
        val pluginManager = project.pluginManager

        pluginManager.apply(Dependencies.Plugins.KOTLIN_ANDROID_APPLY)
        pluginManager.apply(Dependencies.Plugins.ANDROID_APPLICATION_APPLY)


        applyAndroidPlugin(project)

        project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
            compilerOptions {
                languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
                jvmTarget.set(JvmTarget.fromTarget(Versions.Target.jvmTarget))
            }
        }
        project.tasks.register("testClasses")
    }

    private fun applyAndroidPlugin(project: Project) {
        project.dependencies {
            //coreLibraryDesugaring(Dependencies.Plugins.DESUGARING_PLUGIN)
        }
        project.configurations.create("natives")
        project.extensions.configure<BaseAppModuleExtension>("android") {

            namespace =  BuildType.APPLICATION_ID
            compileSdk = Versions.Target.compileSdkVersion

            defaultConfig {
                applicationId = BuildType.APPLICATION_ID
                minSdk = Versions.Target.minSdkVersion
                targetSdk = Versions.Target.targetSdkVersion
                versionCode = 1
                versionName = "1.0.0"

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            }

            buildTypes {
                getByName(BuildType.DEBUG).isDefault = true
                getByName(BuildType.RELEASE) {
                    isMinifyEnabled = false
                    isShrinkResources = false
                    isDebuggable = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            compileOptions {
                // isCoreLibraryDesugaringEnabled = true
                sourceCompatibility = Versions.Target.sourceCompatibility
                targetCompatibility = Versions.Target.targetCompatibility
            }
            buildFeatures {
                // compose = true
                viewBinding = true
            }

            packaging {
                resources {
                    excludes.apply {
                        add("/META-INF/{AL2.0,LGPL2.1}")
                        add("META-INF/DEPENDENCIES.txt")
                        add("META-INF/DEPENDENCIES")
                        add("META-INF/dependencies.txt")
                        add("META-INF/LICENSE")
                        add("META-INF/LICENSE.txt")
                        add("META-INF/NOTICE")
                        add("LICENSE")
                        add("LICENSE.txt")
                        add("NOTICE")
                    }
                    pickFirsts.apply {
                        add("META-INF/LICENSE.txt")
                        add("META-INF/LICENSE")
                        add("META-INF/license.txt")
                        add("META-INF/LGPL2.1")
                        add("META-INF/NOTICE.txt")
                        add("META-INF/NOTICE")
                        add("META-INF/notice.txt")
                    }
                }
            }
        }
    }
}
