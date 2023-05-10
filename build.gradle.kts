/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 09.05.23, 12:09
 *
 */

plugins {
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false
    id("com.squareup.sqldelight") version "1.5.5" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20" apply false
    // id("com.google.devtools.ksp").version("1.7.0-1.0.6")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    jvmArgs = mutableListOf("--enable-preview")
}