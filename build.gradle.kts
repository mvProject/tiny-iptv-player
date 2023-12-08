/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 16:56
 *
 */

plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.android.library") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("app.cash.sqldelight") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    jvmArgs = mutableListOf("--enable-preview")
}