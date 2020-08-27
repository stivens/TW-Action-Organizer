package io.github.stiv3ns.twactionorganizer.logging

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerReport
import kotlinx.coroutines.channels.SendChannel
import kotlin.reflect.KClass

sealed class LogMessage

internal class SubscribeRequest(
    val messageType: KClass<out LogMessage>,
    val channel: SendChannel<LogMessage>
) : LogMessage()

internal class UnsubscribeRequest(
    val messageType: KClass<out LogMessage>,
    val channel: SendChannel<LogMessage>
) : LogMessage()

class Info(val text: String) : LogMessage()
class Warn(val text: String) : LogMessage()
class Error(val text: String) : LogMessage()

class Report(val assignerReport: AssignerReport) : LogMessage()