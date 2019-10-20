/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package me.timebox.jvm

import me.timebox.CountdownTimer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

private val DEBUG_TIME_OVERRIDE_SECONDS: Long? = null

private const val DELAY_BETWEEN_NOTIFICATIONS_SECONDS = 15L

private val dateFormat = SimpleDateFormat("kk:mm:ss")
private val notifications = JvmNotifications()

// TODO: feature ideas
// - play/pause
// - custom messages (i.e. reasons for sleeping & passed to notifications
// - notification sounds (Linux or DnD macOS)
fun main(args: Array<String>) {
    if (args.size != 1) {
        printUsage()
        exitProcess(1)
    }

    println("Started at ${dateFormat.format(Date())}")

    val timerMinutes: Long = args[0].toLong()
    val timerMillis = minutesToMillisWithOverride(timerMinutes)
    val timer = newCountdownTimer(timerMillis).apply {
        addUpdateListener { onUpdate(timerMinutes, it) }
    }

    timer.play()
}

private fun printUsage() {
    println("usage: timebox <minutes>")
}

private fun minutesToMillisWithOverride(timerMinutes: Long): Long {
    return when (DEBUG_TIME_OVERRIDE_SECONDS) {
        null -> TimeUnit.MINUTES.toMillis(timerMinutes)
        else -> {
            println("  TIME OVERRIDE ENABLED")
            TimeUnit.SECONDS.toMillis(DEBUG_TIME_OVERRIDE_SECONDS)
        }
    }
}

private fun onUpdate(totalDurationMins: Long, update: CountdownTimer.Update) {
    fun clearLine() = print("\b".repeat(40)) // count is arbitrary

    clearLine()

    if (update.state == CountdownTimer.State.COMPLETED) {
        println("Completed at ${dateFormat.format(Date())}")

        while (true) { // We expect the user to kill the app with ^-c.
            notifications.send("Timebox.me complete", "Slept $totalDurationMins minutes.")
            Thread.sleep(TimeUnit.SECONDS.toMillis(DELAY_BETWEEN_NOTIFICATIONS_SECONDS))
        }
    }

    print("  ${update.durationRemaining} remaining")
}

private fun newCountdownTimer(totalDurationMillis: Long) = CountdownTimer(
    startTimeMillis = System.currentTimeMillis(),
    totalDurationMillis = totalDurationMillis,
    backingTimer = JvmTimer(),
    getTimeMillis = { System.currentTimeMillis() }
)
