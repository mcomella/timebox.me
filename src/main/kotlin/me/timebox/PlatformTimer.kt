/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package me.timebox

interface PlatformTimer {
    fun scheduleAtFixedRate(delay: Long, period: Long, onUpdate: () -> Unit): Task

    interface Task {
        fun cancel()
    }
}
