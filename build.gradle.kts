// build.gradle (project root)
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath "com.android.tools.build:gradle:9.4.0"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20"
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// Prevent deprecated or dynamic dependency issues
tasks.register("clean", Delete) {
    delete rootProject.buildDir
}
