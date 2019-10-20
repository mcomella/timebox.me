/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package me.timebox

class Duration(val millis: Long) {

    // TODO: breaks for durations > 99
    override fun toString(): String {
        val minutes = millis / 1000 /* seconds */ / 60
        val minutesAsMillis = minutes * 60 * 1000

        val remainingSeconds = millis - minutesAsMillis
        val seconds = remainingSeconds / 1000

        val minutesStr = "0$minutes".takeLast(3)
        val secondsStr = "0$seconds".takeLast(2)

        return "$minutesStr:$secondsStr"
    }
}
