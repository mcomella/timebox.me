/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package me.timebox.jvm

import me.timebox.PlatformTimer
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class JvmTimer : PlatformTimer {

    private val backingTimer = Timer()

    override fun scheduleAtFixedRate(delay: Long, period: Long, onUpdate: () -> Unit): PlatformTimer.Task {
        val backingTimerTask = backingTimer.scheduleAtFixedRate(
            delay = delay,
            period = period
        ) { onUpdate() }

        return Task(backingTimerTask)
    }

    class Task(private val backingTask: TimerTask) : PlatformTimer.Task {
        override fun cancel() {
            backingTask.cancel()
        }
    }
}
