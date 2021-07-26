/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package me.timebox.jvm

import java.lang.UnsupportedOperationException
import java.util.concurrent.TimeUnit

class JvmNotifications {

    private val runtime = Runtime.getRuntime()

    fun send(title: String, bodyText: String) {
        when (Hardware.os) {
            Hardware.OS.LINUX -> sendLinux(title, bodyText)
            Hardware.OS.MAC_OS -> sendMac(title, bodyText)
            Hardware.OS.WINDOWS -> sendWindows()
            Hardware.OS.UNKNOWN -> throw UnsupportedOperationException("Unknown OS type: ${Hardware.osRaw}")
        }
    }

    private fun sendLinux(title: String, bodyText: String) {
        val expireMillis = TimeUnit.SECONDS.toMillis(4).toString()
        val cmd = arrayOf("notify-send",
                "--urgency", "low", // lets other notifications override.
                "--expire-time", expireMillis,
                title, bodyText)
        runtime.exec(cmd).waitAndThrowOnNonZero()
    }

    private fun sendMac(title: String, bodyText: String) {
        // TODO: test.
        val cmd = arrayOf("osascript", "-e", "display notification \"$bodyText\" with title \"$title\" sound name \"Submarine\"")
        runtime.exec(cmd).waitAndThrowOnNonZero()
    }

    private fun sendWindows() {
        throw NotImplementedError()
    }
}

private fun Process.waitAndThrowOnNonZero() {
    waitFor()
    if (exitValue() != 0) {
        val errorText = errorStream.bufferedReader().readText()
        throw IllegalStateException("Notification command ended in non-0 exit value: \"$errorText\"")
    }
}
