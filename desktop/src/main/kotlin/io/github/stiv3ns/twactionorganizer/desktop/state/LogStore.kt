package io.github.stiv3ns.twactionorganizer.desktop.state

import androidx.compose.runtime.mutableStateListOf
import io.github.stiv3ns.twactionorganizer.logging.logs.ErrorLog
import io.github.stiv3ns.twactionorganizer.logging.logs.InfoLog
import io.github.stiv3ns.twactionorganizer.logging.logs.ReportLog
import io.github.stiv3ns.twactionorganizer.logging.logs.WarnLog
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class LogLevel { INFO, WARN, ERROR, REPORT }

data class LogEntry(
    val timestamp: String,
    val level: LogLevel,
    val message: String
)

class LogStore {
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    val entries = mutableStateListOf<LogEntry>()

    private fun now(): String = LocalTime.now().format(timeFormatter)

    fun addInfo(text: String) {
        entries.add(LogEntry(now(), LogLevel.INFO, text))
    }

    fun addWarn(text: String) {
        entries.add(LogEntry(now(), LogLevel.WARN, text))
    }

    fun addError(text: String) {
        entries.add(LogEntry(now(), LogLevel.ERROR, text))
    }

    fun addReport(name: String, unassigned: Int) {
        val msg = if (unassigned > 0) {
            "$name: $unassigned targets could not be assigned"
        } else {
            "$name: all targets assigned successfully"
        }
        entries.add(LogEntry(now(), LogLevel.REPORT, msg))
    }

    suspend fun startSubscriptions() {
        InfoLog.subscribe { log -> addInfo(log.text) }
        WarnLog.subscribe { log -> addWarn(log.text) }
        ErrorLog.subscribe { log -> addError(log.text) }
        ReportLog.subscribe { log ->
            addReport(log.report.name, log.report.unassignedTargets.size)
        }
    }
}
