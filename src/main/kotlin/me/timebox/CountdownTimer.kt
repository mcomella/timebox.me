/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package me.timebox

import me.timebox.CountdownTimer.State.*

typealias UpdateListener = (update: CountdownTimer.Update) -> Unit

private class TimerTask(
    val backingTask: PlatformTimer.Task,
    val timerStartMillis: Long,
    val durationRemainingAtStartMillis: Duration
)

// todo: licenses. not thread safe.
class CountdownTimer(
    private val startTimeMillis: Long, // todo: necessary? Only output in update.
    totalDurationMillis: Long,
    private val backingTimer: PlatformTimer,
    private val getTimeMillis: () -> Long
) {

    private var state = PAUSED
    private var durationRemaining = Duration(totalDurationMillis)

    private val updateListeners = mutableListOf<UpdateListener>()

    private var latestTimerTask: TimerTask? = null

    fun play() {
        state = PLAYING
        backingTimer.scheduleAtFixedRate(
            delay = 1000L,
            period = 1000L
        ) {
            // todo: define which thread this is called on
            val timerTask = latestTimerTask!!
            val millisPassed = getTimeMillis() - timerTask.timerStartMillis
            val durationRemainingMillis = timerTask.durationRemainingAtStartMillis.millis - millisPassed

            if (durationRemainingMillis <= 0) {
                state = COMPLETED
                durationRemaining = Duration(0)
                cancelTimerTask()
            } else {
                durationRemaining = Duration(durationRemainingMillis)
            }

            callUpdateListeners()
        }.also {
            latestTimerTask = TimerTask(it, timerStartMillis = getTimeMillis(), durationRemainingAtStartMillis = durationRemaining)
        }

        callUpdateListeners()
    }

    fun pause() {
        state = PAUSED
        cancelTimerTask()

        callUpdateListeners()
    }

    fun togglePlayPause() {
        when (state) {
            PLAYING -> pause()
            PAUSED -> play()
            else -> { /* do nothing */ }
        }
    }

    private fun cancelTimerTask() {
        latestTimerTask?.backingTask?.cancel()
        latestTimerTask = null
    }

    fun addUpdateListener(listener: UpdateListener) {
        updateListeners.add(listener)
    }

    private fun callUpdateListeners() {
        val update = Update(
            state,
            startTimeMillis,
            durationRemaining
        )
        updateListeners.forEach { it.invoke(update) }
    }

    enum class State {
        PLAYING, PAUSED, COMPLETED;
    }

    data class Update(
        val state: State,
        val startTimeMillis: Long,
        val durationRemaining: Duration
    )
}
