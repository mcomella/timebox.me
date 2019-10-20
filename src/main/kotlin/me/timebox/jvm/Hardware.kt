/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package me.timebox.jvm

object Hardware {

    enum class OS {
        MAC_OS, LINUX, WINDOWS, UNKNOWN
    }

    val osRaw = System.getProperty("os.name") ?: "UNAVAILABLE"
    val os = when (osRaw) {
        "Linux" -> OS.LINUX
        else -> OS.MAC_OS // TODO: unknown.
    }
}
